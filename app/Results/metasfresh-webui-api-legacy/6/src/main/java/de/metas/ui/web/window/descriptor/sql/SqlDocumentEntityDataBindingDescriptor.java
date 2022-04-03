package de.metas.ui.web.window.descriptor.sql;
 import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.adempiere.ad.expression.api.ICachedStringExpression;
import org.adempiere.ad.expression.api.IExpressionFactory;
import org.adempiere.ad.expression.api.IStringExpression;
import org.adempiere.ad.expression.api.impl.CompositeStringExpression;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.lang.IPair;
import org.compiere.model.POInfo;
import org.compiere.util.DB;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.metas.security.IUserRolePermissions;
import de.metas.security.impl.AccessSqlStringExpression;
import de.metas.security.permissions.Access;
import de.metas.ui.web.window.datatypes.DocumentId;
import de.metas.ui.web.window.descriptor.DetailId;
import de.metas.ui.web.window.descriptor.DocumentEntityDataBindingDescriptor;
import de.metas.ui.web.window.descriptor.DocumentFieldDataBindingDescriptor;
import de.metas.ui.web.window.model.DocumentQueryOrderBy;
import de.metas.ui.web.window.model.DocumentQueryOrderByList;
import de.metas.ui.web.window.model.DocumentsRepository;
import de.metas.ui.web.window.model.sql.SqlDocumentQueryBuilder;
import de.metas.util.Check;
import de.metas.util.Services;
import lombok.NonNull;
import lombok.ToString;
public class SqlDocumentEntityDataBindingDescriptor implements SqlEntityBinding,DocumentEntityDataBindingDescriptor{

 private  String TABLEALIAS_Master;

 public  String FIELDNAME_Version;

 private  DocumentsRepository documentsRepository;

 private  String sqlTableName;

 private  String sqlTableAlias;

 private  String sqlLinkColumnName;

 private  String sqlParentLinkColumnName;

 private  ICachedStringExpression sqlSelectAllFrom;

 private  ICachedStringExpression sqlWhereClause;

 private  DocumentQueryOrderByList defaultOrderBys;

 private  ImmutableMap<String,SqlDocumentFieldDataBindingDescriptor> _fieldsByFieldName;

 private  ImmutableList<SqlDocumentFieldDataBindingDescriptor> keyFields;

 private  Optional<String> sqlSelectVersionById;

 private  SqlDocumentEntityDataBindingDescriptor _built;

 private  DocumentsRepository documentsRepository;

 private  String _sqlTableName;

 private  String _tableAlias;

 private  String _sqlLinkColumnName;

 private  String _sqlParentLinkColumnName;

 private  String _sqlWhereClause;

 private  IStringExpression _sqlWhereClauseExpression;

 private  IStringExpression _sqlSelectAll;

 private  LinkedHashMap<String,SqlDocumentFieldDataBindingDescriptor> _fieldsByFieldName;


public Builder setTableName(String sqlTableName){
    assertNotBuilt();
    _sqlTableName = sqlTableName;
    return this;
}


public String getSqlParentLinkColumnName(){
    return _sqlParentLinkColumnName;
}


public boolean isSingleKey(){
    return keyFields.size() == 1;
}


public Map<String,SqlDocumentFieldDataBindingDescriptor> getFieldsByFieldName(){
    return _fieldsByFieldName;
}


public IStringExpression buildSqlWhereClause(){
    if (Check.isEmpty(_sqlWhereClause, true)) {
        return IStringExpression.NULL;
    }
    final String sqlWhereClausePrepared = _sqlWhereClause.trim().replace(getTableName() + ".", // 
    getTableAlias() + ".");
    final IStringExpression sqlWhereClauseExpr = Services.get(IExpressionFactory.class).compileOrDefault(sqlWhereClausePrepared, IStringExpression.NULL, IStringExpression.class);
    return sqlWhereClauseExpr;
}


public IStringExpression getSqlWhereClauseExpression(){
    if (_sqlWhereClauseExpression == null) {
        _sqlWhereClauseExpression = buildSqlWhereClause();
    }
    return _sqlWhereClauseExpression;
}


public DocumentsRepository getDocumentsRepository(){
    Check.assumeNotNull(documentsRepository, "Parameter documentsRepository is not null");
    return documentsRepository;
}


@Override
public SqlDocumentFieldDataBindingDescriptor getFieldByFieldName(String fieldName){
    final SqlDocumentFieldDataBindingDescriptor field = _fieldsByFieldName.get(fieldName);
    if (field == null) {
        throw new IllegalArgumentException("No field found for fieldName=" + fieldName + " in " + this);
    }
    return field;
}


public Builder setDocumentsRepository(DocumentsRepository documentsRepository){
    assertNotBuilt();
    this.documentsRepository = documentsRepository;
    return this;
}


public Builder setTableAliasFromDetailId(DetailId detailId){
    if (detailId == null) {
        setTableAlias(TABLEALIAS_Master);
    } else {
        setTableAlias(detailId.getTableAlias());
    }
    return this;
}


public void assertNotBuilt(){
    if (_built != null) {
        throw new IllegalStateException("Already built: " + this);
    }
}


public SqlDocumentEntityDataBindingDescriptor cast(DocumentEntityDataBindingDescriptor descriptor){
    return (SqlDocumentEntityDataBindingDescriptor) descriptor;
}


public POInfo getPOInfo(){
    return POInfo.getPOInfo(getTableName());
}


public String getSingleKeyColumnName(){
    Check.assume(keyFields.size() == 1, "Single key field: {}", this);
    return keyFields.get(0).getColumnName();
}


public void buildSqlSelects(){
    final Collection<SqlDocumentFieldDataBindingDescriptor> fields = getFieldsByFieldName().values();
    if (fields.isEmpty()) {
        Check.fail("No SQL fields found; this={}", this);
    }
    final List<String> sqlSelectValuesList = new ArrayList<>(fields.size());
    final List<IStringExpression> sqlSelectDisplayNamesList = new ArrayList<>(fields.size());
    for (final SqlDocumentFieldDataBindingDescriptor sqlField : fields) {
        // 
        // Value column
        final SqlSelectValue sqlSelectValue = sqlField.getSqlSelectValue();
        sqlSelectValuesList.add(sqlSelectValue.toSqlStringWithColumnNameAlias());
        // 
        // Display column, if any
        if (sqlField.getSqlSelectDisplayValue() != null) {
            final SqlSelectDisplayValue sqlSelectDisplayValue = sqlField.getSqlSelectDisplayValue();
            sqlSelectDisplayNamesList.add(sqlSelectDisplayValue.toStringExpressionWithColumnNameAlias());
        }
    }
    // 
    _sqlSelectAll = buildSqlSelect(sqlSelectValuesList, sqlSelectDisplayNamesList);
}


public String getParentLinkColumnName(){
    return sqlParentLinkColumnName;
}


public IStringExpression getSqlSelectAll(){
    if (_sqlSelectAll == null) {
        buildSqlSelects();
    }
    return _sqlSelectAll;
}


@Override
public SqlDocumentEntityDataBindingDescriptor getOrBuild(){
    if (_built == null) {
        _built = new SqlDocumentEntityDataBindingDescriptor(this);
    }
    return _built;
}


public Builder builder(){
    return new Builder();
}


public List<SqlDocumentFieldDataBindingDescriptor> getKeyFields(){
    return getFieldsByFieldName().values().stream().filter(SqlDocumentFieldDataBindingDescriptor::isKeyColumn).collect(ImmutableList.toImmutableList());
}


public String getSqlWhereClauseById(DocumentId documentId){
    if (documentId.isInt()) {
        return getSqlWhereClauseById(documentId.toInt());
    } else {
        final Map<String, Object> idPartsByFieldName = SqlDocumentQueryBuilder.extractComposedKey(documentId, getKeyFields());
        final StringBuilder sql = new StringBuilder();
        for (final Map.Entry<String, Object> keyFieldNameAndValue : idPartsByFieldName.entrySet()) {
            String keyFieldName = keyFieldNameAndValue.getKey();
            final Object idPart = keyFieldNameAndValue.getValue();
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(sqlTableName).append(".").append(keyFieldName).append("=").append(DB.TO_SQL(idPart));
        }
        return sql.toString();
    }
}


public String getSqlLinkColumnName(){
    return _sqlLinkColumnName;
}


public Builder setChildToParentLinkColumnNames(IPair<String,String> childToParentLinkColumnNames){
    assertNotBuilt();
    if (childToParentLinkColumnNames != null) {
        _sqlLinkColumnName = childToParentLinkColumnNames.getLeft();
        _sqlParentLinkColumnName = childToParentLinkColumnNames.getRight();
    } else {
        _sqlLinkColumnName = null;
        _sqlParentLinkColumnName = null;
    }
    return this;
}


public Builder setSqlWhereClause(String sqlWhereClause){
    assertNotBuilt();
    Check.assumeNotNull(sqlWhereClause, "Parameter sqlWhereClause is not null");
    _sqlWhereClause = sqlWhereClause;
    return this;
}


@Override
public IStringExpression getSqlWhereClause(){
    return sqlWhereClause;
}


public String getTableName(){
    return _sqlTableName;
}


public Builder addField(DocumentFieldDataBindingDescriptor field){
    assertNotBuilt();
    final SqlDocumentFieldDataBindingDescriptor sqlField = SqlDocumentFieldDataBindingDescriptor.castOrNull(field);
    _fieldsByFieldName.put(sqlField.getFieldName(), sqlField);
    return this;
}


public SqlDocumentFieldDataBindingDescriptor getField(String fieldName){
    final SqlDocumentFieldDataBindingDescriptor field = getFieldsByFieldName().get(fieldName);
    if (field == null) {
        throw new AdempiereException("Field " + fieldName + " not found in " + this);
    }
    return field;
}


public Builder setTableAlias(String sqlTableAlias){
    assertNotBuilt();
    _tableAlias = sqlTableAlias;
    return this;
}


public String getTableAlias(){
    return _tableAlias;
}


public DocumentQueryOrderByList getDefaultOrderBys(){
    // Build the ORDER BY from fields
    return getFieldsByFieldName().values().stream().filter(field -> field.isDefaultOrderBy()).sorted(Comparator.comparing(SqlDocumentFieldDataBindingDescriptor::getDefaultOrderByPriority)).map(field -> DocumentQueryOrderBy.byFieldName(field.getFieldName(), field.isDefaultOrderByAscending())).collect(DocumentQueryOrderByList.toDocumentQueryOrderByList());
}


public String getLinkColumnName(){
    return sqlLinkColumnName;
}


public Collection<SqlDocumentFieldDataBindingDescriptor> getFields(){
    return _fieldsByFieldName.values();
}


public IStringExpression getSqlSelectAllFrom(){
    return sqlSelectAllFrom;
}


@Override
public boolean isVersioningSupported(){
    return sqlSelectVersionById.isPresent();
}


public IStringExpression buildSqlSelect(List<String> sqlSelectValuesList,List<IStringExpression> sqlSelectDisplayNamesList){
    final String sqlTableName = getTableName();
    final String sqlTableAlias = getTableAlias();
    final IStringExpression sqlInnerExpr = IStringExpression.composer().append("SELECT ").append("\n ").append(Joiner.on("\n, ").join(sqlSelectValuesList)).append("\n FROM ").append(sqlTableName).wrap(// security
    AccessSqlStringExpression.wrapper(sqlTableName, IUserRolePermissions.SQL_FULLYQUALIFIED, Access.READ)).build();
    final CompositeStringExpression.Builder sqlBuilder = IStringExpression.composer().append("SELECT ").append("\n").append(sqlTableAlias).append(// Value fields
    ".*");
    // DisplayName fields
    if (!sqlSelectDisplayNamesList.isEmpty()) {
        sqlBuilder.append("\n, ").appendAllJoining("\n, ", sqlSelectDisplayNamesList);
    }
    // FROM
    sqlBuilder.append("\n FROM (").append(sqlInnerExpr).append(") ").append(sqlTableAlias);
    return sqlBuilder.build();
}


public Optional<String> getSqlSelectVersionById(){
    if (getFieldsByFieldName().get(FIELDNAME_Version) == null) {
        return Optional.empty();
    }
    final List<SqlDocumentFieldDataBindingDescriptor> keyColumns = getKeyFields();
    if (keyColumns.size() != 1) {
        return Optional.empty();
    }
    final String keyColumnName = keyColumns.get(0).getColumnName();
    final String sql = "SELECT " + FIELDNAME_Version + " FROM " + getTableName() + " WHERE " + keyColumnName + "=?";
    return Optional.of(sql);
}


@Override
public String toString(){
    // NOTE: keep it short
    return MoreObjects.toStringHelper(this).omitNullValues().add("sqlTableName", sqlTableName).add("sqlTableAlias", sqlTableAlias).toString();
}


}