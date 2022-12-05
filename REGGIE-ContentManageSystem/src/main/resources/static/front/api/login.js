function loginApi(data) {
    return $axios({
      'url': '/front/user/login',
      'method': 'post',
      data
    })
  }

function loginoutApi() {
  return $axios({
    'url': '/front/user/loginout',
    'method': 'post',
  })
}

function sendMsgApi(data) {
    return $axios({
        'url': '/front/user/sms',
        'method': 'post',
        data
    })
}

  