<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

const animationDuration = 6000

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart(val) {
      this.chart = echarts.init(this.$el)
      this.chart.setOption({
          tooltip: {
        trigger: 'item',
        backgroundColor: 'rgba(0,0,0,0.9)',
        formatter: function(params) {
            return params.seriesName + '<br/>' + params.marker + '<span style="color:' + params.color + '">' + params.data['name'] + '\n' + params.data['value'] + '</span>';
        }
    },
    series: [

        {
            name: '',
            type: 'pie',
            hoverAnimation: false,
            legendHoverLink: false,
            radius: ['60%', '70%'],
            center: ['55%', '55%'],
            color: ['rgba(223, 171, 0, 0.3)', 'rgba(169, 45, 45, 0.3)', 'rgba(128, 139, 198, 0.3)'],
            label: {
                normal: {
                    position: 'inner'
                }
            },
            labelLine: {
                normal: {
                    show: false,
                },

            },
            tooltip: {
                show: false,

            },
            data: [{
                    value: val.xunjian,
                    name: ''
                },
                {
                    value: val.abnormalRecordCount,
                    name: ''
                },
                {
                    value: val.jianxiu,
                    name: ''
                }
            ]
        },
        {
            name: '异常情况',
            type: 'pie',
            radius: ['70%', '80%'],
            center: ['55%', '55%'],
            color: ['rgba(249, 191, 0, 1)', 'rgba(169, 45, 45, 1)', 'rgba(128, 139, 198, 1)', ],
            label: {
                normal: {
                    show: false
                },
            },
            labelLine: {
                normal: {
                    smooth: true,
                    length: 10,
                    lineStyle: {
                        width: 1.5
                    }
                }
            },
            itemStyle: {
                normal: {
                    shadowBlur: 15,
                    shadowColor: 'rgba(0, 0, 0, 0.5)',
                    borderColor:'#0a1a2a',
                    // borderWidth:'5',
                }
            },
            data: [{
                    value: val.xunjian,
                    name: '巡检正常'
                },
                {
                    value: val.abnormalRecordCount,
                    name: '巡检异常'
                },
                {
                    value: val.jianxiu,
                    name: '维修记录'
                }
            ]
        }
    ]
      })
    }
  }
}
</script>
