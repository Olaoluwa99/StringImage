package com.easit.image_id

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.random.Random

class IdImage {

    fun getImageFromId(imageId: String): Bitmap {
        val length = imageId.length
        var slashLocation = 0
        var initial = ""
        var id = ""
        //
        val charList = mutableListOf<String>()
        for (i in imageId){
            charList.add(i.toString())
        }
        //
        var stop = false
        charList.forEachIndexed { index, value ->
            if (value != "|"){
                if (!stop){
                    initial = "$initial$value"
                }
            }else {
                slashLocation = index
                stop = true
            }
        }
        //
        for (i in (slashLocation+1)..(length-25)){
            id = "$id${imageId[i]}"
        }
        //
        val startRed = "${imageId[length-24]}${imageId[length-23]}${imageId[length-22]}".toInt()
        val startGreen = "${imageId[length-21]}${imageId[length-20]}${imageId[length-19]}".toInt()
        val startBlue = "${imageId[length-18]}${imageId[length-17]}${imageId[length-16]}".toInt()

        val endRed = "${imageId[length-15]}${imageId[length-14]}${imageId[length-13]}".toInt()
        val endGreen = "${imageId[length-12]}${imageId[length-11]}${imageId[length-10]}".toInt()
        val endBlue = "${imageId[length-9]}${imageId[length-8]}${imageId[length-7]}".toInt()

        val startRedString = "${imageId[length-24]}${imageId[length-23]}${imageId[length-22]}"
        val startGreenString = "${imageId[length-21]}${imageId[length-20]}${imageId[length-19]}"
        val startBlueString = "${imageId[length-18]}${imageId[length-17]}${imageId[length-16]}"

        val endRedString = "${imageId[length-15]}${imageId[length-14]}${imageId[length-13]}"
        val endGreenString = "${imageId[length-12]}${imageId[length-11]}${imageId[length-10]}"
        val endBlueString = "${imageId[length-9]}${imageId[length-8]}${imageId[length-7]}"
        val backgroundShuffleSeed = "${imageId[length-6]}${imageId[length-5]}${imageId[length-4]}${imageId[length-3]}${imageId[length-2]}${imageId[length-1]}"

        val startColor = Color(red = startRed, green = startGreen, blue = startBlue)
        val endColor = Color(red = endRed, green = endGreen, blue = endBlue)

        return fullImageProcess(
            initials = initial, userId = id, color1 = "$startRedString$startGreenString$startBlueString", color2 = "$endRedString$endGreenString$endBlueString",
            width = 1200, height = 1200, startColor = startColor, endColor = endColor,
            textColor = Color.White, shuffleSeed = backgroundShuffleSeed.toLong()
        )
    }

    fun retrieveIdByImage(userImage: Bitmap): Task<Text> {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(userImage, 0)
        return recognizer.process(image)
    }

    fun createUserId(
        mainText: String,
        startColorRed: Int,
        startColorGreen: Int,
        startColorBlue: Int,
        endColorRed: Int,
        endColorGreen: Int,
        endColorBlue: Int,
        userId: String,
        shuffleSeed: Long
    ): String{
        //
        val startRed = structureColorNumber(startColorRed)
        val startGreen = structureColorNumber(startColorGreen)
        val startBlue = structureColorNumber(startColorBlue)
        val endRed = structureColorNumber(endColorRed)
        val endGreen = structureColorNumber(endColorGreen)
        val endBlue = structureColorNumber(endColorBlue)

        return "$mainText|$userId$startRed$startGreen$startBlue$endRed$endGreen$endBlue$shuffleSeed"
    }

    private fun fullImageProcess(
        initials: String,
        userId: String,
        color1: String,
        color2: String,
        width: Int,
        height: Int,
        startColor: Color,
        endColor: Color,
        textColor: Color,
        shuffleSeed: Long
    ): Bitmap {
        val response = setBackground(width, height, startColor, endColor)
        val design = reassembleImage(8, shuffleBitmap(splitImageToParts(8, response), shuffleSeed))
        val firstResult =  setTextsOnBackground(
            background = design,
            //text1 = initials,
            text1 = userId,
            text2 = "$color1$color2",
            text3 = "$shuffleSeed",
            textSize1 = 60f,
            textSize2 = 60f,
            textSize3 = 60f,
            textColor = textColor,
            typeface = Typeface.DEFAULT
        )
        return setTextOnBackgroundCenter(
            background = firstResult,
            text = initials,
            width = width,
            height = height,
            textColor = textColor,
            textSize = 300f
        )
    }

    private fun setTextsOnBackground(
        background: Bitmap,
        text1: String,
        text2: String,
        text3: String,
        textSize1: Float,
        textSize2: Float,
        textSize3: Float,
        textColor: Color,
        typeface: Typeface? = null
    ) : Bitmap {
        val bitmap = Bitmap.createBitmap(background, 0, 0, background.width, background.height)
        val canvas = Canvas(bitmap)
        val spacing = 30f

        // Calculate text bounds for positioning
        val bounds1 = getTextBounds(text1, textSize1, typeface)
        val bounds2 = getTextBounds(text2, textSize2, typeface)
        val bounds3 = getTextBounds(text3, textSize3, typeface)

        // Calculate y positions for bottom-aligned texts
        val bottomY = bitmap.height - 0f // Bottom of text area
        val text3Y = bottomY - bounds3.height() // Align text3 to the bottom
        val text2Y = text3Y - bounds2.height() - spacing // Align text2 above text3 with spacing
        val text1Y = text2Y - bounds1.height() - spacing // Align text1 above text2 with spacing

        // Create paints for each text
        val paint1 = createPaint(textColor, textSize1, typeface)
        val paint2 = createPaint(textColor, textSize2, typeface)
        val paint3 = createPaint(textColor, textSize3, typeface)

        // Draw texts at calculated positions
        canvas.drawText(text1, bitmap.width / 2f, text1Y, paint1)
        canvas.drawText(text2, bitmap.width / 2f, text2Y, paint2)
        canvas.drawText(text3, bitmap.width / 2f, text3Y, paint3)

        return bitmap
    }

    private fun getTextBounds(text: String, textSize: Float, typeface: Typeface? = null): Rect {
        val paint = createPaint(Color(0,0,0), textSize, typeface)
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return bounds
    }

    private fun createPaint(color: Color, textSize: Float, typeface: Typeface? = null): Paint {
        return Paint().apply {
            this.color = color.toArgb()
            this.textSize = textSize
            this.textAlign = Paint.Align.CENTER
            this.typeface = typeface ?: Typeface.DEFAULT
        }
    }

    private fun setBackground(width: Int, height: Int, startColor: Color, endColor: Color) : Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val gradient = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(startColor.toArgb(), endColor.toArgb()), null, Shader.TileMode.CLAMP
        )

        val paint = Paint().apply {
            this.shader = gradient
        }

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }

    private fun structureColorNumber(colorValur: Int): String{
        //val x = (1..255).random()
        return if (colorValur < 10){
            "00$colorValur"
        }else if (colorValur in 10..99){
            "0$colorValur"
        }else "$colorValur"
    }

    private fun setTextOnBackgroundCenter(
        background: Bitmap,
        text: String,
        width: Int,
        height: Int,
        textColor: Color,
        textSize: Float,
    ) : Bitmap {
        val bitmap = Bitmap.createBitmap(background, 0, 0, width, height)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            this.color = textColor.toArgb()
            this.textSize = textSize
            this.typeface = Typeface.DEFAULT_BOLD
        }

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)

        val x = (width - bounds.width()) / 2
        val y = (height + bounds.height()) / 2

        canvas.drawText(text, x.toFloat(), y.toFloat(), paint)

        return bitmap
    }

    private fun reassembleImage(imagePartsRoot: Int, tiles: List<Bitmap>): Bitmap {
        // Reassemble the image into a new bitmap
        val imageWidth = tiles[0].width * imagePartsRoot
        val imageHeight = tiles[0].height * imagePartsRoot
        val shuffledBitmap = Bitmap.createBitmap(imageWidth, imageHeight, tiles[0].config)
        val canvas = Canvas(shuffledBitmap)

        for (y in 0 until imagePartsRoot) {
            for (x in 0 until imagePartsRoot) {
                val partIndex = y * imagePartsRoot + x
                val partBitmap = tiles[partIndex]
                canvas.drawBitmap(partBitmap, (x * partBitmap.width).toFloat(), (y * partBitmap.height).toFloat(), null)
            }
        }

        return shuffledBitmap
    }

    private fun shuffleBitmap(tiles: List<Bitmap>, userSeed: Long): List<Bitmap> {
        val random = Random(userSeed)//Random
        val tilesCopy = tiles.toMutableList()
        return tilesCopy.shuffled(random)
    }

    private fun splitImageToParts(imagePartsRoot: Int, bitmap: Bitmap): List<Bitmap> {
        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        val tileWidth = imageWidth / imagePartsRoot
        val tileHeight = imageHeight / imagePartsRoot
        val tiles = mutableListOf<Bitmap>()

        // Split image into 16 parts
        for (y in 0 until imagePartsRoot) {
            for (x in 0 until imagePartsRoot) {
                val croppedBitmap = Bitmap.createBitmap(
                    bitmap,
                    x * tileWidth, y * tileHeight,
                    tileWidth, tileHeight
                )
                tiles.add(croppedBitmap)
            }
        }

        return tiles
    }
}