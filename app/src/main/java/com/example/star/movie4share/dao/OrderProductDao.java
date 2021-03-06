package com.example.star.movie4share.dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.example.star.movie4share.entity.OrderProduct;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ORDER_PRODUCT".
*/
public class OrderProductDao extends AbstractDao<OrderProduct, Long> {

    public static final String TABLENAME = "ORDER_PRODUCT";

    /**
     * Properties of entity OrderProduct.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property ProductId = new Property(1, long.class, "productId", false, "PRODUCT_ID");
        public final static Property ProductNum = new Property(2, int.class, "productNum", false, "PRODUCT_NUM");
        public final static Property Memo = new Property(3, String.class, "memo", false, "MEMO");
    }

    private Query<OrderProduct> order_OrderProductQuery;

    public OrderProductDao(DaoConfig config) {
        super(config);
    }
    
    public OrderProductDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ORDER_PRODUCT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"PRODUCT_ID\" INTEGER NOT NULL ," + // 1: productId
                "\"PRODUCT_NUM\" INTEGER NOT NULL ," + // 2: productNum
                "\"MEMO\" TEXT);"); // 3: memo
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ORDER_PRODUCT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, OrderProduct entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getProductId());
        stmt.bindLong(3, entity.getProductNum());
 
        String memo = entity.getMemo();
        if (memo != null) {
            stmt.bindString(4, memo);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, OrderProduct entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getProductId());
        stmt.bindLong(3, entity.getProductNum());
 
        String memo = entity.getMemo();
        if (memo != null) {
            stmt.bindString(4, memo);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public OrderProduct readEntity(Cursor cursor, int offset) {
        OrderProduct entity = new OrderProduct( //
            cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // productId
            cursor.getInt(offset + 2), // productNum
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // memo
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, OrderProduct entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setProductId(cursor.getLong(offset + 1));
        entity.setProductNum(cursor.getInt(offset + 2));
        entity.setMemo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(OrderProduct entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(OrderProduct entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(OrderProduct entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "orderProduct" to-many relationship of Order. */
    public List<OrderProduct> _queryOrder_OrderProduct(long productId) {
        synchronized (this) {
            if (order_OrderProductQuery == null) {
                QueryBuilder<OrderProduct> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ProductId.eq(null));
                order_OrderProductQuery = queryBuilder.build();
            }
        }
        Query<OrderProduct> query = order_OrderProductQuery.forCurrentThread();
        query.setParameter(0, productId);
        return query.list();
    }

}
