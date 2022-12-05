function loginApi(data) {
  return $axios({
    'url': '/backend/employee/login',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/backend/employee/logout',
    'method': 'post',
  })
}
