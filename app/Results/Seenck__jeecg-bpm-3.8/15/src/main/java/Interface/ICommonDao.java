package Interface;
public interface ICommonDao {

   public Object getAllDbTableName(Object Object);
   public Object getAllDbTableSize(Object Object);
   public Object save(Object Object);
   public Object saveOrUpdate(Object Object);
   public Object delete(Object Object);
   public Object deleteAllEntitie(Object Object);
   public Object get(Object Object);
   public Object loadAll(Object Object);
   public Object getEntity(Object Object);
   public Object findUniqueByProperty(Object Object);
   public Object findByProperty(Object Object);
   public Object singleResult(Object Object);
   public Object deleteEntityById(Object Object);
   public Object updateEntitie(Object Object);
   public Object findByQueryString(Object Object);
   public Object updateBySqlString(Object Object);
   public Object findListbySql(Object Object);
   public Object findByPropertyisOrder(Object Object);
   public Object getPageList(Object Object);
   public Object getDataTableReturn(Object Object);
   public Object getDataGridReturn(Object Object);
   public Object getPageListBySql(Object Object);
   public Object getSession(Object Object);
   public Object findByExample(Object Object);
   public Object getListByCriteriaQuery(Object Object);
   public T uploadFile(UploadFile uploadFile);
   public HttpServletResponse viewOrDownloadFile(UploadFile uploadFile);
   public HttpServletResponse createXml(ImportFile importFile);
   public void parserXml(String fileName);
   public List<ComboTree> comTree(List<TSDepart> all,ComboTree comboTree);
   public List<ComboTree> ComboTree(List all,ComboTreeModel comboTreeModel,List in,boolean recursive);
   public List<TreeGrid> treegrid(List<?> all,TreeGridModel treeGridModel);
   public Object createQuery(Object Object);
   public Object executeSql(Object Object);
   public Object executeSqlReturnKey(Object Object);
   public Object findForJdbc(Object Object);
   public Object findForJdbcParam(Object Object);
   public Object findObjForJdbc(Object Object);
   public Object findOneForJdbc(Object Object);
   public Object getCountForJdbc(Object Object);
   public Object getCountForJdbcParam(Object Object);
   public Object batchSave(Object Object);
   public Object findHql(Object Object);
   public Object pageList(Object Object);
   public Object findByDetached(Object Object);
   public Object executeProcedure(Object Object);
}