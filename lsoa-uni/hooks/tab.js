import { reactive } from "vue"



export const useTab = () => {
	const tab =  reactive({
		tabIndex:0,
		list:[]
	})
	
	const handlerTabSelect = (index) => {
		tab.tabIndex = index
	}
		
	return {tab, handlerTabSelect}
} 