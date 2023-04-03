package odoo.miem.android.common.uiKitComponents.headers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import odoo.miem.android.common.uiKitComponents.icon.ProfileIcon
import odoo.miem.android.common.uiKitComponents.R
import odoo.miem.android.common.uiKitComponents.text.HeadlineText
import odoo.miem.android.core.uiKitTheme.mainHorizontalPadding

/**
 * [CommonModuleHeader] is header of any module
 *
 * @param userName is name of user :)
 * @param avatarUrl also link of user's avatar. By default used [R.drawable.default_user_avatar]
 *
 * @author Vorozhtsov Mikhail
 */
@Composable
fun CommonModuleHeader(
    userName: String = stringResource(R.string.default_user_name),
    avatarUrl: String? = null
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(
            horizontal = mainHorizontalPadding
        ),
    horizontalArrangement = Arrangement.SpaceBetween
) {
    val iconSize = 40.dp

    Column {
        HeadlineText(
            textRes = R.string.hello_text,
            isLarge = false
        )

        HeadlineText(text = userName)
    }

    IconButton(
        onClick = { /*TODO Implement profile click*/ }
    ) {
        ProfileIcon(
            avatarUrl = avatarUrl,
            userName = userName
        )
    }
}
