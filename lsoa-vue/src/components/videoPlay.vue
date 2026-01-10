<template>
  <div class="hello-ezuikit-js">
    <div id="video-container" :style="{'width':width+'px','height':height+'px'}"></div>
    <!-- <div>
      <button v-on:click="stop">stop</button>
      <button v-on:click="play">play</button>
      <button v-on:click="openSound">openSound</button>
      <button v-on:click="closeSound">closeSound</button>
      <button v-on:click="startSave">startSave</button>
      <button v-on:click="stopSave">stopSave</button>
      <button v-on:click="capturePicture">capturePicture</button>
      <button v-on:click="fullScreen">fullScreen</button>
      <button v-on:click="getOSDTime">getOSDTime</button>
      <button v-on:click="ezopenStartTalk">开始对讲</button>
      <button v-on:click="ezopenStopTalk">结束对讲</button>
      <button v-on:click="destroy">销毁</button>
    </div> -->
  </div>
</template>

<script>
// import {getYsToken} from "@/api/ruodian"
import EZUIKit from "ezuikit-js";
var player = null;

export default {
  props: {
    width:{
      type:String,
      default:500
    },
    height:{
      type:String,
      default:500
    },
    type:{
      type:String,
      default:'security'
    }
  },
  mounted: () => {
  },
  methods: {
   async init(xuliehao,tongdaohao){
       var data=await getYsToken()
       var accessToken=data.msg
       //console.log(accessToken)
      //  var accessToken = 'at.4em0xa13073fhkzt31vlxqu96gn7ucnp-7zpwsn155w-0xgf7xx-oar2bh1w5';
        player = new EZUIKit.EZUIKitPlayer({
          id: 'video-container', // 视频容器ID
          accessToken: accessToken,
          audio:0,
          url: 'ezopen://open.ys7.com/'+xuliehao+'/'+tongdaohao+'.live',
          // simple - 极简版; pcLive-pc直播；pcRec-pc回放；mobileLive-移动端直播；mobileRec-移动端回放;security - 安防版;voice-语音版;
          template: this.type,
          plugin: ['talk'], // 加载插件，talk-对讲
          height: this.height,
          width:this.width
        });
        window.player = player;
    },
    play() {
      var playPromise = player.play();
      playPromise.then((data) => {
        console.log("promise 获取 数据", data)
      })
    },
    stop() {
      var stopPromise = player.stop();
      stopPromise.then((data) => {
        console.log("promise 获取 数据", data)
      })
    },
    getOSDTime() {
      var getOSDTimePromise = player.getOSDTime();
      getOSDTimePromise.then((data) => {
        console.log("promise 获取 数据", data)
      })
    },
    capturePicture() {
      var capturePicturePromise = player.capturePicture(`${new Date().getTime()}`);
      capturePicturePromise.then((data) => {
        console.log("promise 获取 数据", data)
      })
    },
    openSound() {
            var openSoundPromise = player.openSound();
      openSoundPromise.then((data) => {
        console.log("promise 获取 数据", data)
      })
    },
    closeSound() {
      var openSoundPromise = player.closeSound();
      openSoundPromise.then((data) => {
        console.log("promise 获取 数据", data)
      })
    },
    startSave() {
            var startSavePromise = player.startSave(`${new Date().getTime()}`);
      startSavePromise.then((data) => {
        console.log("promise 获取 数据", data)
      })
    },
    stopSave() {
      var stopSavePromise = player.stopSave();
      stopSavePromise.then((data) => {
        console.log("promise 获取 数据", data)
      })
    },
    ezopenStartTalk() {
      player.startTalk();
    },
    ezopenStopTalk() {
      player.stopTalk();
    },
    fullScreen() {
      player.fullScreen();
    },
    destroy() {
      var destroyPromise = player.destroy();
      destroyPromise.then((data) => {
        console.log("promise 获取 数据", data)
      })
    }
  }
};
</script>
