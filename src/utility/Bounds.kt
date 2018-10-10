package utility

data class Bounds(val leftOffset: Double, val topOffset: Double, val rightOffset: Double, val bottomOffset: Double) {

    fun collidesWith(thisPosition: Double2, otherPosition: Double2, otherBounds: Bounds): Boolean {
        return thisPosition.x + this.leftOffset < otherPosition.x + otherBounds.rightOffset &&
                thisPosition.x + this.rightOffset > otherPosition.x + otherBounds.leftOffset &&
                thisPosition.y + this.topOffset < otherPosition.y + otherBounds.bottomOffset &&
                thisPosition.y + this.bottomOffset > otherPosition.y + otherBounds.topOffset
    }

}