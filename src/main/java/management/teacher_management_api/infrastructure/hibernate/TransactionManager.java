package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
@Service(value = "customTxManager")
public class TransactionManager {
    private final PlatformTransactionManager txManager;

    public void tx(TransactionCallbackWithoutResult cb) {
        val tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        tt.execute(
                txStatus -> {
                    cb.doInTransaction(txStatus);
                    return null;
                });
    }

    public <T> T returnFromTx(TransactionCallback<T> cb) {
        val tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return tt.execute(cb);
    }
}
