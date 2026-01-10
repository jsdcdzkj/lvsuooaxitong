export default {
    data() {
        return {
            /**分页数据**/
            page: {
                pageNo: 1,
                pageSize: 10,
            },
            /**表数据**/
            table: {
                records: [],
                // 数量
                total: 0,
                // 加载中
                loading: false,
                api: null,
                extraKey: '',
                cacheParams: {},
            },
        };
    },
    methods: {
        /**
         * 分页改变事件
         * @param {Number} page
         * @param {Number} limit
         */
        async onPaginationChange({ page, limit }) {
            this.page.pageNo = page;
            this.page.pageSize = limit;
            await this.fetchTableData();
        },
        /** 请求数据 **/
        async fetchTableData(callback) {
            this.table.loading = !this.table.loading;
            try {
                const res = await this.table.api(
                    Object.assign(
                        {},
                        callback
                            ? callback(Reflect.get(this, this.table.extraKey))
                            : Reflect.get(this, this.table.extraKey),
                        this.page
                    )
                );
                if (res.code === 0) {
                    this.table.records = res.data.records.map(val => {
                        if(val.cust) {
                            return {
                                ...val,
                                client: val.cust.customerName,
                                userName: val.cust.lawyerNames,
                                delegate:  val.cust.customerCode ? 1 : 2,
                                invoice: val.cust.invoice,
                                createTime: val.cust.createTime,
                                id: val.cust.id
                            }
                        } else {
                            return val
                        }
                    });
                    this.table.total = res.data.total;
                    if (this.scrollTop) {
                        this.scrollTop();
                    }
                }
            } catch (e) {
                console.error(e);
            } finally {
                this.table.loading = !this.table.loading;
            }
        },
        /**
         * 绑定请求接口
         * @param reqApi {Function} 接口
         * @param extraKey {String} 额外参数
         * @param isStartRequest {Boolean} 是否立即立即请求接口
         */
        async bindList(reqApi, extraKey, isStartRequest = true) {
            this.table.api = reqApi;
            this.table.extraKey = extraKey;
            this.table.cacheParams = { ...this[extraKey] };
            if (isStartRequest) {
                await this.fetchTableData();
            }
        },
        // 序号
        indexMethod(index) {
            return (this.page.pageNo - 1) * this.page.pageSize + index + 1;
        },
        handlerSearch(callback) {
            this.page.pageNo = 1;
            this.fetchTableData(callback);
        },
        handlerRefresh(callback) {
            this.page.pageNo = 1;
            this[this.table.extraKey] = { ...this.table.cacheParams };
            this.fetchTableData(callback);
        },
    },
};
