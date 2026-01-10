import defaultSettings from '@/settings'

const title = defaultSettings.title || '仲裁院联调中心业务平台'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
