import request from '@/utils/request'

/** 删除 */
export function deleteRow(roleId) {
    return request({
        url: `/${obj.humpClassName}/delete`,
        method: 'post',
        data: [roleId]
    })
}

/** 新增 */
export function add(data) {
    return request({
        url: '/${obj.humpClassName}/add',
        method: 'post',
        data
    })
}

/** 编辑 */
export function edit(data) {
    return request({
        url: '/${obj.humpClassName}/update',
        method: 'post',
        data
    })
}
