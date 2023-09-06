import {ElNotification} from 'element-plus'

export function openSuccessNotification(msg: string) {
  ElNotification({
    title: 'Successful',
    message: msg,
    type: 'success',
  })
}

export function openErrorNotification(msg: string) {
  ElNotification({
    title: 'Error',
    message: msg,
    type: 'error',
  })
}
