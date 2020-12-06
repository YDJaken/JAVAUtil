import ObjectUtil from "./ObjectUtil";
import Check from "./Check";

export default class xhrManager {
    constructor(obj) {
        this.setProperty(obj);
        this.init();
    }

    init() {
        this.xhrs = [];
        for (let i = 0; i < this.netNumber; i++) {
            this.xhrs.push(new XMLHttpRequest());
        }
        this.queryList = [];
    }

    _loadActiveNet() {
        for (let i = 0; i < this.netNumber; i++) {
            if (this.xhrs[i].status === XMLHttpRequest.UNSENT) {
                return this.xhrs[i];
            }
        }
        return undefined;
    }

    /**
     *  统一的设置入口
     * @param {Object} obj - 传入的配置项
     */
    setProperty(obj) {
        ObjectUtil.setProperties(this, obj);
    }

    setNetNumber(netNumber) {
        this.netNumber = Check.number(netNumber) ? netNumber : 5;
        return this;
    }

    _startQuery(target, resolve, reject) {
        target.open(option.method, option.url);
        if (Check.checkDefined(option.header)) {
            for (let header in option.header) {
                if (option.header.hasOwnProperty(header)) {
                    target.setRequestHeader(header, option.header[header]);
                }
            }
        }
        if (Check.number(options.timeout)) {
            target.timeout = options.timeout;
        } else {
            target.timeout = 0;
        }
        if (option.method === "POST") {
            target.send(option.data);
        } else {
            target.send();
        }
        target.ontimeout = (data) => {
            this._processQuery();
            reject(data, 'timeout');
        }
        target.onabort = (data) => {
            reject(data, 'abort');
        }
        target.onerror = (data) => {
            this._processQuery();
            reject(data, 'error');
        }
        target.onload = (data) => {
            this._processQuery();
            resolve(data);
        }
    }

    _processQuery(options) {
        if (Check.checkDefined(options)) {
            this.queryList.push(options);
        }
        let target = this._loadActiveNet();
        let promise = undefined;
        if (!Check.checkDefined(target)) {
            if (Check.checkDefined(options)) {
                promise = new Promise((resolve, reject) => {
                    options.promise = (target) => {
                        if (!Check.checkDefined(target)) {
                            reject(undefined, "cancel");
                        } else {
                            this._startQuery(target, resolve, reject);
                        }
                    }
                });
            }
        } else {
            let option = this.queryList.shift();
            if (Check.function(option.promise)) {
                option.promise(target);
            } else {
                promise = new Promise((resolve, reject) => {
                    this._startQuery(target, resolve, reject);
                });
            }
        }
        return promise;
    }

    get(url, options = {}) {
        options.url = url;
        options.method = "GET";
        return this._processQuery(options);
    }

    post(url, data, options = {}) {
        if (!Check.checkDefined(data)) {
            throw new Error("data is needed");
        }
        options.url = url;
        options.method = "POST";
        options.data = data;
        return this._processQuery(options);
    }


    cancelAll() {
        for (let i = 0; i < this.netNumber; i++) {
            if (this.xhrs[i].status !== XMLHttpRequest.UNSENT) {
                this.xhrs[i].abort();
            }
        }
        for (let i = 0; i < this.queryList; i++) {
            if (Check.checkDefined(this.queryList[i].promise)) {
                this.queryList[i].promise();
            }
        }
    }

    destroy() {
        this.cancelAll();
        ObjectUtil.delete(this);
    }
}