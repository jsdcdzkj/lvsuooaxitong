//rem 缩放

if (window.addEventListener) {
  window.addEventListener('resize', function () {
      setHtmlFontSize()
  }, false)
}
export function setHtmlFontSize () {
  // 1366是设计稿的宽度，当大于1366时采用1366宽度，比例也是除以13.66
    //deviceWidth = document.documentElement.clientWidth > 1366 ? 1366 : document.documentElement.clientWidth
  	var deviceWidth = document.documentElement.clientWidth
    return document.getElementsByTagName('html')[0].style.cssText = 'font-size:' + deviceWidth / 1920 + 'px !important'
}

//全屏
export function fullScreen() {
    var element = document.documentElement;
    if (element.requestFullscreen) {
        element.requestFullscreen();
    } else if (element.msRequestFullscreen) {
        element.msRequestFullscreen();
    } else if (element.mozRequestFullScreen) {
        element.mozRequestFullScreen();
    } else if (element.webkitRequestFullscreen) {
        element.webkitRequestFullscreen();
    }
}

//退出全屏
export function exitFullscreen() {
    if (document.exitFullscreen) {
        document.exitFullscreen();
    } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
    } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
    } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen();
    }
}
