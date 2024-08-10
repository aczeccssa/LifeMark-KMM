package data.resources

object TermConditions {
    data class Term(val title: String, val content: List<String>)

    const val TITLE = "LifeMark Terms and Service"

    const val EFFECTIVE_DATE = "Effective Date: August 9, 2024"

    const val INTRODUCTION = "Welcome to LifeMark! These terms of service (\"Terms\") govern your use of the LifeMark application (\"App\" or \"Service\"), which includes any updates, enhancements, and features we may provide. By installing, accessing, or using the App, you agree to these Terms. If you do not agree with these Terms, please do not use the App."

    val TERMS = listOf(
        Term("Acceptance of Terms", listOf(
            "You must accept these Terms before using the App.",
            "We may update these Terms from time to time. Continued use of the App after any changes constitutes acceptance of the updated Terms."
        )),
        Term("Eligibility", listOf(
            "You must be at least 13 years old to use the App.",
            "If you are under 18, you must have parental consent to use the App.",
        )),
        Term(" Account Registration and Security", listOf(
            "You may need to register for an account to access some features of the App.",
            "You are responsible for maintaining the confidentiality of your login credentials.",
            "You must promptly notify us if you suspect unauthorized use of your account.",
        )),
        Term("User Content", listOf(
            "You retain ownership of all content you submit, post, or display through the App.",
            "By submitting content, you grant us a worldwide, non-exclusive, royalty-free license to use, reproduce, distribute, and display such content in connection with the App.",
            "You represent and warrant that you own or have the necessary rights to all content you submit.",
        )),
        Term("Intellectual Property Rights", listOf(
            "The App and its contents, including but not limited to text, graphics, logos, images, and software, are owned by or licensed to us.",
            "All rights not expressly granted to you are reserved.",
        )),
        Term("Privacy", listOf(
            "Your use of the App is subject to our Privacy Policy, which describes how we collect, use, and disclose information.",
            "We are committed to protecting your privacy and the security of your personal data.",
        )),
        Term(" Prohibited Activities", listOf(
            "You agree not to use the App in any way that violates these Terms or any applicable laws.",
            "You may not engage in any activity that interferes with or disrupts the operation of the App.",
        )),
        Term("Termination", listOf(
            "We reserve the right to terminate or suspend your access to the App without notice if you violate these Terms.",
            "Upon termination, all rights granted to you under these Terms will cease.",
        )),
        Term("Warranty Disclaimer", listOf(
            "The App is provided on an 'as is' and 'as available' basis.",
            "We do not warrant that the App will be uninterrupted or error-free.",
        )),
        Term("Limitation of Liability", listOf(
            "In no event shall we be liable for any indirect, incidental, special, consequential, or punitive damages arising out of or in connection with your use of the App.",
        )),
        Term("Indemnification", listOf(
            "You agree to indemnify and hold harmless LifeMark and its affiliates, officers, agents, and employees from any claim or demand arising out of your use of the App.",
        )),
        Term("Governing Law and Jurisdiction", listOf(
            "These Terms shall be governed by and construed in accordance with the laws of the jurisdiction where the company is headquartered.",
            "Any disputes arising out of these Terms shall be resolved in the courts of that jurisdiction.",
        )),
        Term("Contact Us", listOf(
            "For any questions about these Terms, please contact us at [support@lifemark.com].",
        )),
        Term(" Additional Information", listOf(
            "Please review the [Privacy Policy] and other policies posted on our website for more information.",
        )),
    )
}