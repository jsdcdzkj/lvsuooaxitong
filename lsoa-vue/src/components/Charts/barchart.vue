<template>
    <div ref="maincharts" :class="className" :style="{ height: height, width: width }" />
</template>

<script>
// import echarts from 'echarts'
import echarts from 'echarts'; //建议按需导入，不会占用项目太多资源
require('echarts/theme/macarons'); // echarts theme
import resize from './mixins/resize';

const animationDuration = 6000;

export default {
    mixins: [resize],
    props: {
        className: {
            type: String,
            default: 'chart',
        },
        width: {
            type: String,
            default: '100%',
        },
        height: {
            type: String,
            default: '300px',
        },
        valdata: {
            type: Object,
            default: {
                caseCount: [],
                time: [],
            },
        },
    },
    data() {
        return {
            chart: null,
        };
    },
    watch: {
        valdata(val) {
            this.initChart();
        },
    },
    mounted() {
        this.$nextTick(() => {
            this.initChart();
        });
    },
    beforeDestroy() {
        if (!this.chart) {
            return;
        }
        this.chart.dispose();
        this.chart = null;
    },
    methods: {
        initChart() {
            //console.log(this.valdata)
            this.chart?.clear()
            this.chart = echarts.init(this.$el);

            const option = {
                grid: {
                    left: '100px',
                    top: '20px',
                    right: '12px',
                    bottom: '20px',
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                    },
                    formatter: '{b} : {c}',
                },
                legend: {
                    show: true,
                    icon: 'circle',
                    orient: 'horizontal',
                    top: '90.5%',
                    right: 'center',
                    itemWidth: 16.5,
                    itemHeight: 6,
                    // itemGap: 30,
                    textStyle: {
                        // color: '#FFFFFF'
                        color: '#C9C8CD',
                        fontSize: 14,
                    },
                },
                xAxis: [
                    {
                        data: this.valdata.time,
                        axisLabel: {
                            textStyle: {
                                color: '#aaaaaa',
                                fontSize: 14,
                            },
                            margin: 10, //刻度标签与轴线之间的距离。
                        },
                        axisLine: {
                            show: true,
                            lineStyle: {
                                width: 1,
                                color: 'rgba(0,0,0,0.45)',
                            },
                        },
                        axisTick: {
                            show: false,
                        },
                        axisLabel: {
                            fontSize: 14,
                        },
                        boundaryGap: true,
                        splitLine: {
                            show: false,
                            width: 0.08,
                            lineStyle: {
                                type: 'solid',
                                color: 'rgba(0,0,0,0.06)',
                            },
                        },
                    },
                ],
                yAxis: [
                    {
                        splitLine: {
                            show: true,
                            lineStyle: {
                                color: 'rgba(0,0,0,0.15)',
                                type: 'dotted',
                            },
                        },
                        axisLine: {
                            show: false,
                        },
                        axisTick: {
                            show: false,
                        },
                        axisLabel: {
                            fontSize: 14,
                            color: 'rgba(0,0,0,0.45)'
                        },
                        minInterval: 1,
                    },
                ],
                series: [
                    //柱体
                    {
                        name: '',
                        type: 'bar',
                        barWidth: 20,
                        barGap: '0%',
                        itemStyle: {
                            normal: {
                                color: {
                                    x: 0,
                                    y: 0,
                                    x2: 0,
                                    y2: 1,
                                    type: 'linear',
                                    global: false,
                                    colorStops: [
                                        //第一节下面
                                        // {
                                        //     offset: 0,
                                        //     color: '#2386C5',
                                        // },
                                        {
                                            offset: 0,
                                            color: '#45B6FF',
                                        },
                                        {
                                            
                                            offset: 1,
                                            color: '#198ED9',
                                        },
                                    ],
                                },
                            },
                        },

                        data: this.valdata.caseCount,
                    },

                    //柱顶圆片
                    {
                        name: '',
                        type: 'pictorialBar',
                        symbolSize: [20, 10],
                        symbolOffset: [0, -6],
                        z: 12,
                        symbolPosition: 'end',
                        itemStyle: {
                            normal: {
                                color: new echarts.graphic.LinearGradient(
                                    0,
                                    0,
                                    0,
                                    1,
                                    [
                                        {
                                            offset: 0,
                                            color: '#2689FF',
                                        },
                                        {
                                            offset: 1,
                                            color: '#2CB7F2',
                                        },
                                    ],
                                    false
                                ),
                            },
                        },
                        data: this.valdata.caseCount,
                    },
                ],
            };

            this.chart.setOption(option);
        },
    },
};
</script>
