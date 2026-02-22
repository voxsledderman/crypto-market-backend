package org.voxsledderman.cryptoExchange.infrastructure.persistence.daos;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@DatabaseTable(tableName = "wallets")
public class WalletDao {

    @DatabaseField(id = true, columnName = "owner_uuid", canBeNull = false)
    private UUID ownerUuid;

    @ForeignCollectionField(foreignFieldName = "wallet")
    private ForeignCollection<TradeOrderDao> orders;


}
