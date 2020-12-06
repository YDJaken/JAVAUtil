import Check, {getTimestamp} from "../util/Check";
import ArrayUtil from "../util/ArrayUtil";
import ObjectUtil from "../util/ObjectUtil";

let intresNumber = 0;

export default class HistoryManager {
    constructor(limit) {
        this.limit = Check.number(limit) ? limit : 15;
        this.history = [];
    }

    loadByKeyWord(keyWord) {
        for (let i = 0; i < this.history.length; i++) {
            let target = this.history[i];
            if (target.searchKeyWord === keyWord) {
                return i;
            }
        }
        return -1;
    }

    _sort() {
        this.history.sort((a, b) => {
            let intresA = a.intresNum;
            let intresB = b.intresNum;
            if (intresA > intresB) {
                return -1;
            } else if (intresA === intresB) {
                let timeA = a.time;
                let timeB = b.time;
                if (timeA > timeB) {
                    return -1;
                } else if (timeA === timeB) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        });
    }

    addHistory(response) {
        this.removeIfNeed();
        response.time = getTimestamp();
        response.intresNum = -1;
        this.history.unshift(response);
    }

    removeIfNeed() {
        if (this.history.length >= this.limit) {
            while (this.history.length >= this.limit) {
                let remove = this.history.pop();
                ObjectUtil.delete(remove);
            }
        }
    }

    remove(index) {
        let remove = ArrayUtil.removeIndex(index, this.history);
        ObjectUtil.delete(remove);
    }

    loadHistory(index) {
        let target = this.history[index];
        if (Check.checkDefined(target)) {
            target.intresNum = intresNumber++;
            this._sort();
        }
        return target;
    }

    destroy() {
        ObjectUtil.delete(this);
    }
}