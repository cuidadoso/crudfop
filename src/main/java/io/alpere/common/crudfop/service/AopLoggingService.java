package io.alpere.common.crudfop.service;

import com.google.common.base.Joiner;
import io.alpere.common.crudfop.audit.AuditProvider;
import io.alpere.common.crudfop.model.BaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.alpere.common.crudfop.util.Logging.logInfo;

@Log
@RequiredArgsConstructor
@Aspect
@Order()
@Component
public class AopLoggingService {

    private final AuditProvider auditorAware;

    /**
     * Validator/logger for find all methods
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.common.crudfop.service.CrudService.findAll(..))"
    )
    public void onAfterFindAll(final JoinPoint joinPoint) {
        logInfo("Find all entities of [%s]. Total count of entities is %d. User: [%s]",
                entityName(joinPoint), getListCount(joinPoint), auditorAware.user());
    }

    /**
     * Validator/logger for getting one entity
     *
     * @param joinPoint Join point object
     * @param entity    Entity.
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.common.crudfop.service.CrudService.findOne(..))",
            returning = "entity"
    )
    public void onAfterFindOne(final JoinPoint joinPoint, final BaseEntity entity) {
        logInfo("Find one entity [%s] with id [%d]. User: [%s]",
                entityName(joinPoint), entity.getId(), auditorAware.user());
    }

    /**
     * Validator/logger for save/update entity
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.common.crudfop.service.CrudService.save(..))"
    )
    public void onAfterSave(final JoinPoint joinPoint) {
        logInfo("Save/update entity/entities [%s] with ID/IDs [%s]. User: [%s]",
                entityName(joinPoint), convertArguments(joinPoint), auditorAware.user());
    }

    /**
     * Validator/logger for delete entity
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.common.crudfop.service.CrudService.delete(..))"
    )
    public void onAfterDelete(final JoinPoint joinPoint) {
        logInfo("Delete entity/entities [%s] with ID/IDs [%s]. User: [%s]",
                entityName(joinPoint), convertArguments(joinPoint), auditorAware.user());
    }

    private String entityName(final JoinPoint joinPoint) {
        CrudServiceImpl target = (CrudServiceImpl) joinPoint.getTarget();
        return target.getEntityClass().getName();
    }

    private Long getListCount(final JoinPoint joinPoint) {
        Object arg = joinPoint.getArgs()[0];
        if (arg instanceof List) {
            List list = (List) arg;
            return (long) list.size();
        }
        if (arg instanceof Page) {
            Page page = (Page) arg;
            return page.getTotalElements();
        }
        return 0L;
    }

    private String convertArguments(final JoinPoint joinPoint) {
        Object arg = joinPoint.getArgs()[0];
        if (arg instanceof BaseEntity) {
            return ((BaseEntity) arg).getId().toString();
        }
        if (arg instanceof Iterable) {
            Iterable<BaseEntity> entities = (Iterable) arg;
            List<UUID> result = StreamSupport.stream(entities.spliterator(), false)
                    .map(BaseEntity::getId)
                    .collect(Collectors.toList());
            return Joiner.on(",").join(result);
        }
        return (String) arg;
    }
}
