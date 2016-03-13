SELECT  M.RESOURCE_ID, 
		M.PARENT_RESOURCE_ID, 
		M.MODULE_CODE, 
		M.FUNCTION_CODE,
		M.SEQ, 
		M.MENU_NAME, 
		M.URL, 
		M.IS_LEAF, 
		M.ICON_URL
FROM MENU M
WHERE 1=1
  AND M.RESOURCE_ID = :id