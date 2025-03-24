export const isMobile = () => {

  let userAgentInfo = navigator.userAgent;
  let Agents = ['Android', 'iPhone', 'SymbianOS', 'Windows Phone', 'iPad', 'iPod'];
  let getArr = Agents.filter(i => userAgentInfo.includes(i));
  return getArr.length ? true : false;
};

export const isWeixin = () => {
  var ua = navigator.userAgent.toLowerCase();
  if (ua.match(/MicroMessenger/i) == "micromessenger") {
    return true;
  } else {
    return false;
  }
}

export const isAndroidOrIos = () => {
  var u = navigator.userAgent;
  var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
  var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
  if (isAndroid) {
    return 'isAndroid'
  }
  if (isiOS) {
    return 'isIos'
  }
  return 'isAndroid'
}

export const timestampToDayNoTime = timestamp => {
  let t = Number(timestamp);
  if (typeof t !== 'number' || !t) return timestamp;
  var date = new Date(t * 1000);
  var Y = date.getFullYear() + '-';
  var M = (date.getMonth() + 1).toString().padStart(2, '0') + '-';
  var D = (date.getDate()).toString().padStart(2, '0');
  return Y + M + D;
}

export function toHHmmss(seconds) {
  let hour = Math.floor(seconds / 3600) >= 10 ? Math.floor(seconds / 3600) : '0' + Math.floor(seconds / 3600);
  seconds -= 3600 * hour;
  let min = Math.floor(seconds / 60) >= 10 ? Math.floor(seconds / 60) : '0' + Math.floor(seconds / 60);
  seconds -= 60 * min;
  let sec = seconds >= 10 ? seconds : '0' + seconds;
  return hour + ':' + min + ':' + sec;

}


export const timestampToSeconds = (timestamp, str = '.') => {
  let t = Number(timestamp);
  if (typeof t !== 'number' || !t) return timestamp;
  var date = new Date(t * 1000);
  var Y = date.getFullYear() + str;
  var M = (date.getMonth() + 1).toString().padStart(2, '0') + str;
  var D = (date.getDate()).toString().padStart(2, '0') + ' ';
  var h = (date.getHours()).toString().padStart(2, '0') + ':';
  var m = (date.getMinutes()).toString().padStart(2, '0') + ':';
  var s = (date.getSeconds()).toString().padStart(2, '0');
  return Y + M + D + h + m + s;
}

export const base64toBlob = (base64Data, contentType) => {
  contentType = contentType || '';
  const sliceSize = 1024;
  const byteCharacters = atob(base64Data);
  const byteArrays = [];
  for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
    const slice = byteCharacters.slice(offset, offset + sliceSize);
    const byteNumbers = new Array(slice.length);
    for (let i = 0; i < slice.length; i++) {
      byteNumbers[i] = slice.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    byteArrays.push(byteArray);
  }
  const blob = new Blob(byteArrays, { type: contentType });
  return blob;
}

export const getAllChatMessage = (chatList) => {
  const arr = JSON.parse(JSON.stringify(chatList));
  const len = arr.length;
  const n = 2;
  const Num = len % 2 === 0 ? len / 2 : Math.floor(len / 2 + 1);
  const res = [];
  for (let i = 0; i < Num; i++) {
    const newArr = arr.slice(i * n, i * n + n);
    res.push(newArr);
  }
  let _list = res.map((item) => {
    if (item.length == 1) {
      return [item[0].value];
    }
    if (item.length == 2) {
      return [item[0].value, String(item[1].value)];
    }
    return [];
  });
  return _list;
};

export const validateURL = (url) => {
  var regex = /^(https?:\/\/)?(www\.)?[a-zA-Z0-9-]+(\.[a-zA-Z]+)+([/?].*)?$/;
  return regex.test(url);
}

export const validateWeChatURL = (url) => {
  var regex = /^(http|https):\/\/mp\.weixin\.qq\.com\/.*$/;
  return regex.test(url);
}

export function throttle(fn, timeout = 500) {
  let timer = null,
    last;

  return function (...args) {
    const now = +new Date();

    if (last && now < last + timeout) {
      clearTimeout(timer);
      timer = setTimeout(() => {
        last = now;
        fn.apply(this, args);
      }, timeout);
    } else {
      last = now;
      fn.apply(this, args);
    }
  };
}

// 防抖函数
export function debounce(func, delay) {
  let timeoutId;
  return (...args) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => {
      func.apply(this, args);
    }, delay);
  };
};

export function bytesToSize(number) {

  if (number >= 1000000) {
    const formattedNumber = (number / 1000000).toFixed(1);
    return formattedNumber.endsWith(".0") ? formattedNumber.slice(0, -2) + "M" : formattedNumber + "M";
  }
  if (number >= 1000) {
    const formattedNumber = (number / 1000).toFixed(1);
    return formattedNumber.endsWith(".0") ? formattedNumber.slice(0, -2) + "K" : formattedNumber + "K";
  }
  return number;
}