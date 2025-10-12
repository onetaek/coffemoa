import request from '@/axios'

// Get all dictionaries
export const getDictApi = () => {
  return request.get({ url: '/mock/dict/list' })
}

// Simulate obtaining a dictionary
export const getDictOneApi = async () => {
  return request.get({ url: '/mock/dict/one' })
}
