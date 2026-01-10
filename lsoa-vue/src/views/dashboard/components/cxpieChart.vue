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
    initChart() {
      this.chart = echarts.init(this.$el)
      this.chart.setOption({
         "title": {
        "text": "车行数据",
        "x": "center",
        "y": "center",
        "textStyle": {
            "color": "#fff",
            "fontSize": 12,
        },
    },
    "series": [{
        "type": "pie",
        "center": ["50%", "50%"],
        "radius": ["45%", "53%"],
        "color": ["#15ABAC", "#4C3829"],
        "label": {
            "show": false
        },
        "data": [{
            "name": "在线设备",
            "value": 55
        }, {
            "name": "离线设备",
            "value": 45
        }
        ]
    }, {
        "type": "pie",
        "center": ["50%", "50%"],
        "radius": ["35%", "35.5%"],
        "label": {
            "show": false
        },
        "data": [{
            "value": 78,
            "name": "实例1",
            "itemStyle": {
                "normal": {
                    "color": '#69430F'
                }
            }
        }]
    }]
      })
    }
  }
}
</script>
