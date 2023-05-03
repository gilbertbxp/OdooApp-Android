package odoo.miem.android.feature.crm.impl.data

import odoo.miem.android.common.uiKitComponents.screen.recruitmentLike.model.RecruitmentLikeStatusModel

internal data class Status(
    override val statusName: String,
    override val employees: List<Employee>,
    override val id: Int = 0
) : RecruitmentLikeStatusModel<Employee>
