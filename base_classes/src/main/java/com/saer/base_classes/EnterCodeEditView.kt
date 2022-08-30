package com.saer.base_classes

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import com.saer.core.utils.getPixels
import com.saer.core.utils.lastNotNullIndexFromArray
import kotlin.math.max
import kotlin.properties.Delegates

class EnterCodeEditView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = R.attr.enterCodeEditViewStyle,
    defStyleRes: Int = R.style.DefaultEnterCodeEditViewStyle
) : androidx.appcompat.widget.AppCompatEditText(context, attr, defStyleAttr), TextWatcher {

    private var numberOfDigits: Int = DEF_NUMBER_OF_DIGITS
        set(value) {
            measureText = ""
            repeat(value) { index ->
                measureText += index.toString()
            }
            filters = arrayOf<InputFilter>(LengthFilter(value))

            digitRectangles.clear()
            repeat(value) {
                digitRectangles.add(RectF())
            }

            field = value
            updateViewSizes()
            requestLayout()
            invalidate()
        }

    private var code = arrayOfNulls<Char?>(numberOfDigits)
    private var currentDigit: Int = 0
        set(value) {
            text?.let {
                if (value in 0 until lastNotNullIndexFromArray(code)) {
                    setSelection(value)
                } else if (value >= lastNotNullIndexFromArray(code)) {
                    setSelection(it.length)
                }
            }
            field = if (value >= numberOfDigits) -1 else value
            invalidate()
        }

    private var bordersColor: Int = DEF_BORDERS_COLOR
    private var digitColor: Int = DEF_DIGIT_COLOR
    private var cornerRounding: Float = DEF_CORNER_ROUNDING

    private val digitRectangles: MutableList<RectF> = mutableListOf()

    private val fieldRect = RectF(0f, 0f, 0f, 0f)
    private var digitWidth = 0f
    private var digitHeight = 0
    private var digitPadding = getPixels(16f)
    private val innerPadding = getPixels(16f)

    private val rectanglePaint: Paint
    private val currentRectanglePaint: Paint
    private val digitPaint: Paint

    private var measureText: String = ""

    private var animator: ValueAnimator? = null
    private var animatedValue = 0
    private var animatedDigit = -1

    init {
        if (isInEditMode) {
            code.forEachIndexed { index, _ ->
                code[index] = index.toChar()
            }
        }
        if (attr != null) {
            initAttributes(attr, defStyleRes, defStyleAttr)
        }
        super.setFocusable(true)
        super.setFocusableInTouchMode(true)

        isSaveEnabled = true
        isCursorVisible = false
        inputType = InputType.TYPE_CLASS_NUMBER
        isLongClickable = false

        rectanglePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bordersColor
            style = Paint.Style.STROKE
            strokeWidth = getPixels(DEF_RECTANGLE_STROKE)
        }

        currentRectanglePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bordersColor
            style = Paint.Style.STROKE
            strokeWidth = getPixels(3f)
        }

        digitPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = digitColor
            textSize = super.getTextSize()
        }

        val bounds = Rect()
        digitPaint.getTextBounds("12345", 0, 4, bounds)
        digitHeight = bounds.height()

    }

    private fun initAttributes(attr: AttributeSet, defStyleRes: Int, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(
            attr,
            R.styleable.EnterCodeEditView,
            defStyleAttr,
            defStyleRes
        )
        numberOfDigits = typedArray.getInteger(
            R.styleable.EnterCodeEditView_numberOfDigits,
            DEF_NUMBER_OF_DIGITS
        )
        bordersColor =
            typedArray.getColor(R.styleable.EnterCodeEditView_bordersColor, DEF_BORDERS_COLOR)
        digitColor =
            typedArray.getColor(R.styleable.EnterCodeEditView_digitColor, DEF_DIGIT_COLOR)
        cornerRounding =
            typedArray.getDimension(
                R.styleable.EnterCodeEditView_cornerRounding,
                DEF_CORNER_ROUNDING
            )
        typedArray.recycle()
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()!!
        val savedState = SavedState(superState)
        savedState.code = code
        savedState.currentDigit = currentDigit
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        code = savedState.code
        currentDigit = savedState.currentDigit
        animatedDigit = -1
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredCellSizeInPixels = digitPaint.measureText(measureText)

        val desiredWidth = max(
            minWidth,
            desiredCellSizeInPixels.toInt() + paddingLeft + paddingRight + (digitPadding * (numberOfDigits - 1)).toInt() + (innerPadding * numberOfDigits * 2).toInt()
        )

        val desiredHeight =
            max(minHeight, digitHeight + paddingTop + paddingBottom + (innerPadding * 2).toInt())

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateViewSizes()
    }

    private fun updateViewSizes() {
        val safeWidth = width - paddingLeft - paddingRight
        val safeHeight = height - paddingTop - paddingBottom

        digitWidth = safeWidth / numberOfDigits.toFloat()

        fieldRect.left = paddingLeft.toFloat()
        fieldRect.top = paddingTop.toFloat() + getPixels(DEF_RECTANGLE_STROKE) / 2
        fieldRect.right = fieldRect.left + safeWidth
        fieldRect.bottom = fieldRect.top + safeHeight - getPixels(DEF_RECTANGLE_STROKE)
    }

    override fun onDraw(canvas: Canvas) {

        if (fieldRect.width() <= 0) return
        if (fieldRect.height() <= 0) return

        drawRectangle(canvas)
        drawDigits(canvas)
    }

    private fun drawRectangle(canvas: Canvas) {
        digitRectangles.forEachIndexed { i, rectF ->
            when (i) {
                0 -> {
                    rectF.apply {
                        left = fieldRect.left + digitPadding / 2
                        top = fieldRect.top
                        right = fieldRect.left + digitWidth - digitPadding / 2
                        bottom = fieldRect.bottom
                    }
                }
                else -> {
                    rectF.apply {
                        left = digitRectangles[i - 1].right + digitPadding
                        top = fieldRect.top
                        right = left + digitWidth - digitPadding
                        bottom = fieldRect.bottom
                    }
                }
            }

            canvas.drawRoundRect(
                rectF,
                cornerRounding,
                cornerRounding,
                if (i == currentDigit) currentRectanglePaint else rectanglePaint
            )
        }
    }

    private fun drawDigits(canvas: Canvas) {
        canvas.save()
        canvas.clipRect(fieldRect)
        code.forEachIndexed { index, digit ->
            if (digit != null) {
                val width: Float = digitPaint.measureText(digit.toString())

                canvas.drawText(
                    digit.toString(),
                    digitRectangles[index].left + (digitRectangles[index].width() - width) / 2,
                    digitRectangles[index].bottom + if (animatedDigit == index) animatedValue else -((digitRectangles[index].height() - digitHeight) / 2).toInt(),
                    digitPaint
                )
            }
        }
        canvas.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> {
                digitRectangles.forEachIndexed { index, rectF ->
                    if (isTouchInRect(event, rectF)) {
                        if (index < text!!.length) setSelection(index + 1)
                        currentDigit = index
                    }
                }
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    private fun isTouchInRect(event: MotionEvent, rectF: RectF): Boolean =
        event.x >= rectF.left &&
                event.x <= rectF.right &&
                event.y >= rectF.top &&
                event.y <= rectF.bottom

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (lengthBefore != lengthAfter) {
            if (lengthAfter > lengthBefore) {
                code[currentDigit] = text[start]
                startDigitAnimation(currentDigit)
                currentDigit++
            } else {
                val size = if (currentDigit == -1) lastNotNullIndexFromArray(code) else currentDigit
                for (index in size downTo 0) {
                    if (code[index] != null) {
                        code[index] = null
                        currentDigit = index
                        break
                    }
                }
            }
        }
    }

    private fun startDigitAnimation(indexOfAnimatedDigit: Int) {
        animatedDigit = indexOfAnimatedDigit
        animator = ValueAnimator.ofInt(
            digitHeight,
            -((digitRectangles[indexOfAnimatedDigit].height() - digitHeight) / 2).toInt()
        )?.apply {
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                this@EnterCodeEditView.animatedValue = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    override fun afterTextChanged(s: Editable) {}

    fun clearText() {
        setText("")
        for (index in 0 until numberOfDigits) {
            code[index] = null
        }
        currentDigit = 0
    }

    class SavedState : BaseSavedState {

        var code = arrayOfNulls<Char?>(5)
        var currentDigit by Delegates.notNull<Int>()

        constructor(superState: Parcelable) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            code = parcel.readArray(code.javaClass.classLoader) as Array<Char?>
            currentDigit = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeArray(code)
            out.writeInt(currentDigit)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = Array(size) { null }
            }
        }

    }

    companion object {
        private const val DEF_NUMBER_OF_DIGITS = 5
        private const val DEF_CORNER_ROUNDING = 4f
        private const val DEF_BORDERS_COLOR = Color.BLACK
        private const val DEF_DIGIT_COLOR = Color.WHITE
        private const val DEF_RECTANGLE_STROKE = 2f
    }
}