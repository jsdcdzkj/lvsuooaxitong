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
    },
    valdata:{
      type:Object
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    valdata(val) {
      this.initChart()
    },
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
      console.log(this.valdata)
      this.chart = echarts.init(this.$el)
      this.chart.setOption({
            tooltip: {},
            grid: {
                  left: '12%',
                  right: '0%',
                  bottom: '20%',
                  top:'8%'
              },
            xAxis: {
              axisLabel:{color:'#979797'},
              axisLine:{
               lineStyle:{color:'#979797'}   // x轴坐标轴颜色
               },
             axisTick:{
                show:false      // x轴刻度的颜色
                },
                data: ["今日用量","昨日用量"]
            },
            yAxis: {
               axisLabel:{color:'#979797'},
               splitLine:{
               lineStyle:{color:'#979797'}   // x轴坐标轴颜色
               },
               axisLine:{
                 show:false
               },
               axisTick:{
                 show:false   // x轴刻度的颜色
                }
            },
            series: [{
                name: this.valdata.name,
                type: 'bar',
                itemStyle: {
                  normal: {
                      color: function(params) {
                      var colorList = ['#1163FF','#2EBA06'];
                        return colorList[params.dataIndex]
                      }
                  }
                },
                barWidth:'30',
                data: [this.valdata.today, this.valdata.yesterday]
            }]
        })
    }
  }
}
</script>
