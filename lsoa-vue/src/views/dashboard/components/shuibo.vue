<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>

import echarts from 'echarts'
import resize from './mixins/resize'
import '../../../utils/echarts-liquidfill.min.js'
const animationDuration = 6000
export default {
  mixins: [resize],
  data() {
    return {
       chart: null
    }
  },
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
    valNum:{
      type:Number,
      default:0
    }
  },
  mounted(){
    // this.$nextTick(() => {
    //   this.drawLiquidfill()
    // })

  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods:{
    drawLiquidfill(num){
      this.valNum=num;
	      this.chart = echarts.init(this.$el)
        var data = [this.valNum, this.valNum,];
        var option = {
            series: [{
                type: 'liquidFill',
                radius: '80%',
                outline: {
                    show: false
                },
                center: ['50%', '50%'],
                data: data,
                backgroundStyle: {
                    color: 'rgba(255, 255, 255, 0.2)'
                },
                color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                        offset: 1,
                        color: '#f3c94f'
                    }, {
                        offset: 0.5,
                        color: '#f9e07b'
                    }, {
                        offset: 0,
                        color: '#f3c94f'
                    }],
                    globalCoord: false
                },
                label: {
                    normal: {
                        formatter: '',
                    }
                }
            }, ]
        };
	      this.chart.setOption(option)
  		}
  }
}
</script>

<style lang="scss" scoped>




</style>
