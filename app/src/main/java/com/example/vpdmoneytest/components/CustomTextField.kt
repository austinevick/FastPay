package com.example.vpdmoneytest.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    placeholderStyle: TextStyle=TextStyle.Default,
    readOnly: Boolean = false,
    isError: Boolean = false,
    fontSize: TextUnit = 16.sp,
    textColor: Color = Color.Black,
    textAlign: TextAlign = TextAlign.Start,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions= KeyboardActions.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value, onValueChange = onValueChange,
        placeholder = { Text(text = placeholder,
            style=placeholderStyle) },
        shape = RoundedCornerShape(8.dp),
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction),
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        singleLine = true,
        isError = isError,
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp, if (isError) Color.Red else
                    Color.Gray.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            ),
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        textStyle = TextStyle(
            color = textColor,
            fontSize = fontSize,
            textAlign = textAlign,
            fontWeight = FontWeight.W800
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        )
    )
}