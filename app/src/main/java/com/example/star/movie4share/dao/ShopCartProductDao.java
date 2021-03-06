package com.example.star.movie4share.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.star.movie4share.entity.ShopCartProduct;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SHOP_CART_PRODUCT".
*/
public class ShopCartProductDao extends AbstractDao<ShopCartProduct, Long> {

    public static final String TABLENAME = "SHOP_CART_PRODUCT";

    /**
     * Properties of entity ShopCartProduct.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Category = new Property(2, String.class, "category", false, "CATEGORY");
        public final static Property Price = new Property(3, double.class, "price", false, "PRICE");
        public final static Property Number = new Property(4, int.class, "number", false, "NUMBER");
        public final static Property ImgUrl = new Property(5, String.class, "imgUrl", false, "IMG_URL");
        public final static Property Stock = new Property(6, int.class, "stock", false, "STOCK");
    }


    public ShopCartProductDao(DaoConfig config) {
        super(config);
    }
    
    public ShopCartProductDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SHOP_CART_PRODUCT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"CATEGORY\" TEXT," + // 2: category
                "\"PRICE\" REAL NOT NULL ," + // 3: price
                "\"NUMBER\" INTEGER NOT NULL ," + // 4: number
                "\"IMG_URL\" TEXT," + // 5: imgUrl
                "\"STOCK\" INTEGER NOT NULL );"); // 6: stock
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SHOP_CART_PRODUCT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ShopCartProduct entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(3, category);
        }
        stmt.bindDouble(4, entity.getPrice());
        stmt.bindLong(5, entity.getNumber());
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(6, imgUrl);
        }
        stmt.bindLong(7, entity.getStock());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ShopCartProduct entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(3, category);
        }
        stmt.bindDouble(4, entity.getPrice());
        stmt.bindLong(5, entity.getNumber());
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(6, imgUrl);
        }
        stmt.bindLong(7, entity.getStock());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public ShopCartProduct readEntity(Cursor cursor, int offset) {
        ShopCartProduct entity = new ShopCartProduct( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // category
            cursor.getDouble(offset + 3), // price
            cursor.getInt(offset + 4), // number
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // imgUrl
            cursor.getInt(offset + 6) // stock
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ShopCartProduct entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCategory(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPrice(cursor.getDouble(offset + 3));
        entity.setNumber(cursor.getInt(offset + 4));
        entity.setImgUrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setStock(cursor.getInt(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ShopCartProduct entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ShopCartProduct entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ShopCartProduct entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
