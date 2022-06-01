package literefresh.behavior

class Checkpoint {

    enum class Type {
        UNKNOWN, ANCHOR_POINT, TRIGGER_POINT, STOP_POINT
    }

    constructor(config: OffsetConfig, vararg types: Type) {
        this.offsetConfig = config
        putAllTypes(types)
    }

    var mPrevious: Checkpoint? = null
    var mNext: Checkpoint? = null

    var types: MutableMap<Type, Boolean> = mutableMapOf()

    var offsetConfig: OffsetConfig = OffsetConfig()

    fun putAllTypes(types: Array<out Type>) {
        types.forEach {
            this.types.put(it, true)
        }
    }

    fun active(types: Array<out Type>) {
        types.forEach {
            if (this.types.containsKey(it)) {
                this.types.put(it, true)
            }
        }
    }

    fun deactive(types: Array<out Type>) {
        types.forEach {
            if (this.types.containsKey(it)) {
                this.types.put(it, false)
            }
        }
    }

    fun isActive(type: Type): Boolean {
        return types.get(type) ?: false
    }

    fun offset(): Int {
        return offsetConfig.offset
    }

    fun isStopPoint(): Boolean {
        return isActive(Type.STOP_POINT)
    }

    fun isAnchorPoint(): Boolean {
        return isActive(Type.ANCHOR_POINT)
    }

    fun isTriggerPoint(): Boolean {
        return isActive(Type.TRIGGER_POINT)
    }

}