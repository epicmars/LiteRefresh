package literefresh.behavior

class Checkpoint {

    enum class Type {
        UNKNOWN, ANCHOR_POINT, TRIGGER_POINT, STOP_POINT
    }

    var mPrevious: Checkpoint? = null
    var mNext: Checkpoint? = null

    var types: MutableSet<Type> = mutableSetOf()

    var offsetConfig: OffsetConfig = OffsetConfig()

    fun offset() : Int {
        return offsetConfig.offset
    }

    fun isStopPoint() : Boolean {
        return types.contains(Type.STOP_POINT)
    }

    fun isAnchorPoint() : Boolean {
        return types.contains(Type.ANCHOR_POINT)
    }

    fun isTriggerPoint() : Boolean {
        return types.contains(Type.TRIGGER_POINT)
    }

}