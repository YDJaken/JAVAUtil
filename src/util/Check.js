/**
 * @Author DY
 */

// 时间获取函数
var getTimestamps;

if (typeof performance !== 'undefined' && typeof performance.now === 'function' && isFinite(performance.now())) {
    getTimestamps = function () {
        return performance.now();
    };
} else {
    getTimestamps = function () {
        return Date.now();
    };
}

export const getTimestamp = getTimestamps;

/**
 * @class Check
 * @classdesc 检查参数属性和一些安全配置的工具类
 */
export default class Check {
    /**
     * 检查传入参数是否已经定义
     * @param input
     * @returns {Boolean}
     */
    static checkDefined(input) {
        return input !== undefined && input !== null;
    }


    static defaultValue(input, value) {
        if (Check.checkDefined(input)) {
            return input
        } else {
            return value;
        }
    }

    /**
     * 检查传入参数是否为String 类型
     * @param input
     * @returns {Boolean}
     */
    static string(input) {
        return Check.checkDefined(input) && (typeof input === 'string' || input instanceof String);
    }

    /**
     * 检查传入参数是否为number 类型
     * @param input
     * @returns {Boolean}
     */
    static number(input) {
        return Check.checkDefined(input) && (typeof input === 'number' || input instanceof Number) && !Number.isNaN(input);
    }

    /**
     * 检查传入参数是否为function 类型
     * @param input
     * @returns {Boolean}
     */
    static function(input) {
        return Check.checkDefined(input) && (typeof (input) === 'function' || (input instanceof Function));
    }

    /**
     * 检查入参是否为Array
     * @param input
     * @returns {Boolean}
     */
    static Array(input) {
        return Check.checkDefined(input) && Array.isArray(input);
    }

    /**
     * 判断在范围内
     * @param min
     * @param max
     * @param input
     * @return {Boolean}
     */
    static inRange(min, max, input) {
        return input <= max && input >= min;
    }

    /**
     * 检查入参是否为Object
     * @param input
     * @returns {Boolean}
     */
    static Object(input) {
        return Check.checkDefined(input) && (typeof (input) === 'object' || (input instanceof Object));
    }
}

