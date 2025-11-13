import Mock from 'mockjs'
import { SUCCESS_CODE } from '@/constants'

const timeout = 1000

export default [
  // List interface
  {
    url: '/mock/menu/list',
    method: 'get',
    timeout,
    response: () => {
      return {
        code: SUCCESS_CODE,
        data: {
          list: [
            {
              path: '/dashboard',
              component: '#',
              redirect: '/dashboard/analysis',
              name: 'Dashboard',
              status: Mock.Random.integer(0, 1),
              id: 1,
              type: 0,
              parentId: undefined,
              title: 'front page',
              meta: {
                title: 'front page',
                icon: 'vi-ant-design:dashboard-filled',
                alwaysShow: true
              },
              children: [
                {
                  path: 'analysis',
                  component: 'views/Dashboard/Analysis',
                  name: 'Analysis',
                  status: Mock.Random.integer(0, 1),
                  id: 2,
                  type: 1,
                  parentId: 1,
                  title: 'Analysis page',
                  permissionList: [
                    {
                      id: 1,
                      label: 'New',
                      value: 'add'
                    },
                    {
                      id: 2,
                      label: 'edit',
                      value: 'edit'
                    }
                  ],
                  meta: {
                    title: 'Analysis page',
                    noCache: true,
                    permission: ['add', 'edit']
                  }
                },
                {
                  path: 'workplace',
                  component: 'views/Dashboard/Workplace',
                  name: 'Workplace',
                  status: Mock.Random.integer(0, 1),
                  id: 3,
                  type: 1,
                  parentId: 1,
                  title: 'workbench',
                  permissionList: [
                    {
                      id: 1,
                      label: 'New',
                      value: 'add'
                    },
                    {
                      id: 2,
                      label: 'edit',
                      value: 'edit'
                    },
                    {
                      id: 3,
                      label: 'delete',
                      value: 'delete'
                    }
                  ],
                  meta: {
                    title: 'workbench',
                    noCache: true
                  }
                }
              ]
            },
            {
              path: '/external-link',
              component: '#',
              meta: {
                title: 'document',
                icon: 'vi-clarity:document-solid'
              },
              name: 'ExternalLink',
              status: Mock.Random.integer(0, 1),
              id: 4,
              type: 0,
              parentId: undefined,
              title: 'document',
              children: [
                {
                  path: 'https://element-plus-admin-doc.cn/',
                  name: 'DocumentLink',
                  status: Mock.Random.integer(0, 1),
                  id: 5,
                  type: 1,
                  parentId: 4,
                  title: 'document',
                  meta: {
                    title: 'document'
                  }
                }
              ]
            },
            {
              path: '/level',
              component: '#',
              redirect: '/level/menu1/menu1-1/menu1-1-1',
              name: 'Level',
              status: Mock.Random.integer(0, 1),
              id: 6,
              type: 0,
              parentId: undefined,
              title: 'menu',
              meta: {
                title: 'menu',
                icon: 'vi-carbon:skill-level-advanced'
              },
              children: [
                {
                  path: 'menu1',
                  name: 'Menu1',
                  component: '##',
                  status: Mock.Random.integer(0, 1),
                  id: 7,
                  type: 0,
                  parentId: 6,
                  title: 'Menu 1',
                  redirect: '/level/menu1/menu1-1/menu1-1-1',
                  meta: {
                    title: 'Menu 1'
                  },
                  children: [
                    {
                      path: 'menu1-1',
                      name: 'Menu11',
                      component: '##',
                      status: Mock.Random.integer(0, 1),
                      id: 8,
                      type: 0,
                      parentId: 7,
                      title: 'Menu 1-1',
                      redirect: '/level/menu1/menu1-1/menu1-1-1',
                      meta: {
                        title: 'Menu 1-1',
                        alwaysShow: true
                      },
                      children: [
                        {
                          path: 'menu1-1-1',
                          name: 'Menu111',
                          component: 'views/Level/Menu111',
                          status: Mock.Random.integer(0, 1),
                          id: 9,
                          type: 1,
                          parentId: 8,
                          title: 'Menu 1-1-1',
                          meta: {
                            title: 'Menu 1-1-1'
                          }
                        }
                      ]
                    },
                    {
                      path: 'menu1-2',
                      name: 'Menu12',
                      component: 'views/Level/Menu12',
                      status: Mock.Random.integer(0, 1),
                      id: 10,
                      type: 1,
                      parentId: 7,
                      title: 'Menu 1-2',
                      meta: {
                        title: 'Menu 1-2'
                      }
                    }
                  ]
                },
                {
                  path: 'menu2',
                  name: 'Menu2Demo',
                  component: 'views/Level/Menu2',
                  status: Mock.Random.integer(0, 1),
                  id: 11,
                  type: 1,
                  parentId: 6,
                  title: 'Menu 2',
                  meta: {
                    title: 'Menu 2'
                  }
                }
              ]
            },
            {
              path: '/example',
              component: '#',
              redirect: '/example/example-dialog',
              name: 'Example',
              status: Mock.Random.integer(0, 1),
              id: 12,
              type: 0,
              parentId: undefined,
              title: 'Comprehensive example',
              meta: {
                title: 'Comprehensive example',
                icon: 'vi-ep:management',
                alwaysShow: true
              },
              children: [
                {
                  path: 'example-dialog',
                  component: 'views/Example/Dialog/ExampleDialog',
                  name: 'ExampleDialog',
                  status: Mock.Random.integer(0, 1),
                  id: 13,
                  type: 1,
                  parentId: 12,
                  title: 'Comprehensive example-pop-up window',
                  permissionList: [
                    {
                      id: 1,
                      label: 'New',
                      value: 'add'
                    },
                    {
                      id: 2,
                      label: 'edit',
                      value: 'edit'
                    },
                    {
                      id: 3,
                      label: 'delete',
                      value: 'delete'
                    },
                    {
                      id: 4,
                      label: 'Check',
                      value: 'view'
                    }
                  ],
                  meta: {
                    title: 'Comprehensive example-pop-up window'
                  }
                },
                {
                  path: 'example-page',
                  component: 'views/Example/Page/ExamplePage',
                  name: 'ExamplePage',
                  status: Mock.Random.integer(0, 1),
                  id: 14,
                  type: 1,
                  parentId: 12,
                  title: 'Comprehensive example -page',
                  permissionList: [
                    {
                      id: 1,
                      label: 'New',
                      value: 'add'
                    },
                    {
                      id: 2,
                      label: 'edit',
                      value: 'edit'
                    },
                    {
                      id: 3,
                      label: 'delete',
                      value: 'delete'
                    },
                    {
                      id: 4,
                      label: 'Check',
                      value: 'view'
                    }
                  ],
                  meta: {
                    title: 'Comprehensive example -page'
                  }
                },
                {
                  path: 'example-add',
                  component: 'views/Example/Page/ExampleAdd',
                  name: 'ExampleAdd',
                  status: Mock.Random.integer(0, 1),
                  id: 15,
                  type: 1,
                  parentId: 12,
                  title: 'Comprehensive Example -New',
                  meta: {
                    title: 'Comprehensive Example -New',
                    noTagsView: true,
                    noCache: true,
                    hidden: true,
                    showMainRoute: true,
                    activeMenu: '/example/example-page'
                  }
                },
                {
                  path: 'example-edit',
                  component: 'views/Example/Page/ExampleEdit',
                  name: 'ExampleEdit',
                  status: Mock.Random.integer(0, 1),
                  id: 16,
                  type: 1,
                  parentId: 12,
                  title: 'Comprehensive Example -Editor',
                  meta: {
                    title: 'Comprehensive Example -Editor',
                    noTagsView: true,
                    noCache: true,
                    hidden: true,
                    showMainRoute: true,
                    activeMenu: '/example/example-page'
                  }
                },
                {
                  path: 'example-detail',
                  component: 'views/Example/Page/ExampleDetail',
                  name: 'ExampleDetail',
                  status: Mock.Random.integer(0, 1),
                  id: 17,
                  type: 1,
                  parentId: 12,
                  title: 'Comprehensive example -details',
                  meta: {
                    title: 'Comprehensive example -details',
                    noTagsView: true,
                    noCache: true,
                    hidden: true,
                    showMainRoute: true,
                    activeMenu: '/example/example-page'
                  }
                }
              ]
            }
          ]
        }
      }
    }
  }
]
