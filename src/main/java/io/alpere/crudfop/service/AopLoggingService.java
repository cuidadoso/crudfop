package io.alpere.crudfop.service;

import io.alpere.crudfop.audit.AuditProvider;
import io.alpere.crudfop.model.BaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
@Aspect
@Order()
@RequiredArgsConstructor
public class AopLoggingService {

    private final AuditProvider auditorAware;

    /**
     * Validator/logger for find all methods
     *
     * @param joinPoint Join point object
     * @param entities  list of entities
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.CrudService.findAll(..))",
            returning = "entities"
    )
    public void onAfterFindAll(final JoinPoint joinPoint, final List entities) {
        log.debug("Find all entities of {}. Total count of entities is {}. User: {}",
                entityName(joinPoint), entities.size(), user());

    }

    /**
     * Validator/logger for find all methods
     *
     * @param joinPoint Join point object
     * @param page      page of entities
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.CrudService.page(..))",
            returning = "page"
    )
    public void onAfterFindAll(final JoinPoint joinPoint, final Page page) {
        log.debug("Page og entities of {}. Page: {}. Total entities: {}. User: {}",
                entityName(joinPoint), page.getNumber(), page.getTotalElements(), user());
    }

    /**
     * Validator/logger for getting one entity
     *
     * @param joinPoint Join point object
     * @param entity    Entity.
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.CrudService.findOne(..))",
            returning = "entity"
    )
    public void onAfterFindOne(final JoinPoint joinPoint, final BaseEntity entity) {
        log.debug("Find one entity {} with id {}. User: {}",
                entityName(joinPoint), entity.getId(), user());
    }

    /**
     * Validator/logger for save/update entity
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.CrudService.save(..))"
    )
    public void onAfterSave(final JoinPoint joinPoint) {
        log.debug("Save/update entity/entities {} with ID/IDs {}. User: {}",
                entityName(joinPoint), convertArguments(joinPoint), user());
    }

    /**
     * Validator/logger for delete entity to archive
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.CrudService.softDelete(..))",
            returning = "entity"
    )
    public void onAfterSoftDelete(final JoinPoint joinPoint, final BaseEntity entity) {
        log.debug("Delete to archive entity/entities {} with ID/IDs {}. User: {}",
                entityName(joinPoint), convertDeleteArguments(joinPoint), user());
    }

    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.CrudService.softDeleteAll(..))"
    )
    public void onAfterSoftDeleteAll(final JoinPoint joinPoint) {
        log.debug("Delete to archive entity/entities {} with ID/IDs {}. User: {}",
                entityName(joinPoint), convertDeleteArguments(joinPoint), user());
    }

    /**
     * Validator/logger for delete entity to archive
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.CrudService.delete(..))",
            returning = "entity"
    )
    public void onAfterDelete(final JoinPoint joinPoint, final BaseEntity entity) {
        log.debug("Delete entity/entities {} with ID/IDs {}. User: {}",
                entityName(joinPoint), convertDeleteArguments(joinPoint), user());
    }

    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.CrudService.deleteAll(..))"
    )
    public void onAfterDeleteAll(final JoinPoint joinPoint) {
        log.debug("Delete entity/entities {} with ID/IDs {}. User: {}",
                entityName(joinPoint), convertDeleteArguments(joinPoint), user());
    }

    /**
     * Validator/logger for find all methods
     *
     * @param joinPoint Join point object
     * @param entities  list of archived entities
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.ArchiveService.findAll(..))",
            returning = "entities"
    )
    public void onAfterArchivedFindAll(final JoinPoint joinPoint, final List entities) {
        log.debug("Find all archived entities of {}. Total count of entities is {}. User: {}",
                entityName(joinPoint), entities.size(), user());

    }

    /**
     * Validator/logger for getting one archived entity
     *
     * @param joinPoint Join point object
     * @param entity    Archived entity.
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.ArchiveService.findOne(..))",
            returning = "entity"
    )
    public void onAfterArchivedFindOne(final JoinPoint joinPoint, final BaseEntity entity) {
        log.debug("Find one archived entity {} with id {}. User: {}",
                entityName(joinPoint), entity.getId(), user());
    }

    /**
     * Validator/logger for delete entity to archive
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.ArchiveService.restore(..))",
            returning = "entity"
    )
    public void onAfterRestore(final JoinPoint joinPoint, final BaseEntity entity) {
        log.debug("Restore from archive entity/entities {} with ID/IDs {}. User: {}",
                entityName(joinPoint), convertDeleteArguments(joinPoint), user());
    }

    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.ArchiveService.restoreAll(..))"
    )
    public void onAfterRestoreAll(final JoinPoint joinPoint) {
        log.debug("Restore from archive entity/entities {} with ID/IDs {}. User: {}",
                entityName(joinPoint), convertDeleteArguments(joinPoint), user());
    }

    /**
     * Validator/logger for delete entity to archive
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.ArchiveService.delete(..))",
            returning = "entity"
    )
    public void onAfterArchivedDelete(final JoinPoint joinPoint, final BaseEntity entity) {
        log.debug("Delete archived entity/entities {} with ID/IDs {}. User: {}",
                entityName(joinPoint), convertDeleteArguments(joinPoint), user());
    }

    @AfterReturning(
            pointcut = "execution(* io.alpere.crudfop.service.ArchiveService.deleteAll(..))"
    )
    public void onAfterArchivedDeleteAll(final JoinPoint joinPoint) {
        log.debug("Delete archived entity/entities {} with ID/IDs {}. User: {}",
                entityName(joinPoint), convertDeleteArguments(joinPoint), user());
    }
    private String entityName(final JoinPoint joinPoint) {
        CrudServiceImpl target = (CrudServiceImpl) joinPoint.getTarget();
        return target.getEntityClass().getSimpleName();
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
            return result.stream()
                    .map(UUID::toString)
                    .collect(Collectors.joining(","));
        }
        return (String) arg;
    }

    private String convertDeleteArguments(final JoinPoint joinPoint) {
        Object arg = joinPoint.getArgs()[0];
        if (arg instanceof Iterable) {
            Iterable<UUID> ids = (Iterable) arg;
            return StreamSupport.stream(ids.spliterator(), false)
                    .map(UUID::toString)
                    .collect(Collectors.joining(","));
        }
        return arg.toString();
    }

    private String user() {
        String user = auditorAware.user();
        return user.isEmpty() ? "unknown" : user;
    }
}
