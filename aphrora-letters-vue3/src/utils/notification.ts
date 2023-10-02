import {notification} from "ant-design-vue";
import {h} from "vue";
import {SmileOutlined, FrownOutlined} from "@ant-design/icons-vue";

export const openInfoNotification = (message: string, description: string) => {
    notification.info({
        message: `Notice: ${message}`,
        description: `${description}`,
        duration: 3,
        icon: () => h(SmileOutlined, {style: 'color: #ccc'}),
        top: '64px',
    })
}
export const openSuccessfulNotification = (message: string, description: string) => {
    notification.open({
        message: `Notice: ${message}`,
        description: `${description}`,
        duration: 3,
        icon: () => h(SmileOutlined, {style: 'color: #198754'}),
        top: '64px',
    })
}

export const openErrorNotification = (message: string, description: string) => {
    notification.open({
        message: `Error: ${message}`,
        description: `${description}`,
        duration: 3,
        icon: () => h(FrownOutlined, {style: 'color: #dc3545'}),
        top: '64px',
    })
}
