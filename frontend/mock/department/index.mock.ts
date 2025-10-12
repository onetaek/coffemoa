import { toAnyString } from '@/utils'
import Mock from 'mockjs'
import { SUCCESS_CODE } from '@/constants'

const departmentList: any = []

const citys = ['Xiamen Head Office', 'Beijing Branch', 'Shanghai Branch', 'Fuzhou Branch', 'Shenzhen Branch', 'Hangzhou Branch']

for (let i = 0; i < 5; i++) {
  departmentList.push({
    // Department name
    departmentName: citys[i],
    id: toAnyString(),
    createTime: '@datetime',
    // state
    status: Mock.Random.integer(0, 1),
    // Remark
    remark: '@cword(10, 15)',
    children: [
      {
        // Department name
        departmentName: 'R&D Department',
        id: toAnyString(),
        createTime: '@datetime',
        // state
        status: Mock.Random.integer(0, 1),
        // Remark
        remark: '@cword(10, 15)'
      },
      {
        // Department name
        departmentName: 'Product Department',
        id: toAnyString(),
        createTime: '@datetime',
        // state
        status: Mock.Random.integer(0, 1),
        // Remark
        remark: '@cword(10, 15)'
      },
      {
        // Department name
        departmentName: 'Operations Department',
        id: toAnyString(),
        createTime: '@datetime',
        // state
        status: Mock.Random.integer(0, 1),
        // Remark
        remark: '@cword(10, 15)'
      },
      {
        // Department name
        departmentName: 'Marketing Department',
        id: toAnyString(),
        createTime: '@datetime',
        // state
        status: Mock.Random.integer(0, 1),
        // Remark
        remark: '@cword(10, 15)'
      },
      {
        // Department name
        departmentName: 'Sales Department',
        id: toAnyString(),
        createTime: '@datetime',
        // state
        status: Mock.Random.integer(0, 1),
        // Remark
        remark: '@cword(10, 15)'
      },
      {
        // Department name
        departmentName: 'Customer service department',
        id: toAnyString(),
        createTime: '@datetime',
        // state
        status: Mock.Random.integer(0, 1),
        // Remark
        remark: '@cword(10, 15)'
      }
    ]
  })
}

export default [
  // List interface
  {
    url: '/mock/department/list',
    method: 'get',
    response: () => {
      return {
        code: SUCCESS_CODE,
        data: {
          list: departmentList
        }
      }
    }
  },
  {
    url: '/mock/department/table/list',
    method: 'get',
    response: () => {
      return {
        code: SUCCESS_CODE,
        data: {
          list: departmentList,
          total: 5
        }
      }
    }
  },
  {
    url: '/mock/department/users',
    method: 'get',
    timeout: 1000,
    response: ({ query }) => {
      const { pageSize } = query
      // Create data based on pageSize
      const mockList: any = []
      for (let i = 0; i < pageSize; i++) {
        mockList.push(
          Mock.mock({
            // username
            username: '@cname',
            // account
            account: '@first',
            // Mail
            email: '@EMAIL',
            // creation time
            createTime: '@datetime',
            // user id
            id: toAnyString()
          })
        )
      }
      return {
        code: SUCCESS_CODE,
        data: {
          total: 100,
          list: mockList
        }
      }
    }
  },
  // save interface
  {
    url: '/mock/department/user/save',
    method: 'post',
    timeout: 1000,
    response: () => {
      return {
        code: SUCCESS_CODE,
        data: 'success'
      }
    }
  },
  // Delete interface
  {
    url: '/mock/department/user/delete',
    method: 'post',
    response: ({ body }) => {
      const ids = body.ids
      if (!ids) {
        return {
          code: 500,
          message: 'Please select the data to be deleted'
        }
      } else {
        return {
          code: SUCCESS_CODE,
          data: 'success'
        }
      }
    }
  },
  // save interface
  {
    url: '/mock/department/save',
    method: 'post',
    timeout: 1000,
    response: () => {
      return {
        code: SUCCESS_CODE,
        data: 'success'
      }
    }
  },
  // Delete interface
  {
    url: '/mock/department/delete',
    method: 'post',
    response: ({ body }) => {
      const ids = body.ids
      if (!ids) {
        return {
          code: 500,
          message: 'Please select the data to be deleted'
        }
      } else {
        return {
          code: SUCCESS_CODE,
          data: 'success'
        }
      }
    }
  }
]
