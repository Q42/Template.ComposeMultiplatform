# Ensure all Previews have been stripped
-checkdiscard class * {
    @androidx.compose.ui.tooling.preview.Preview <methods>;
}
-keepclassmembers,allowshrinking class * {
    @androidx.compose.ui.tooling.preview.Preview <methods>;
}