import request from '@/utils/request'

export function getroutes(data) {
  return request({
    url: 'api/menu/list',
    method: 'post',
    data
  })
}
