package io.alpere.crud.service

import io.alpere.crud.audit.AuditProvider
import io.alpere.crud.model.BaseEntity
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.data.domain.Page
import mu.KotlinLogging

// Static logger
private val logger = KotlinLogging.logger {}

@Aspect
class AopLoggingService(private val auditorAware: AuditProvider) {
    /**
     * Validator/logger for find all methods
     *
     * @param joinPoint Join point object
     * @param entities  list of entities
     */
    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.CrudService.findAll(..))", returning = "entities")
    fun onAfterFindAll(joinPoint: JoinPoint, entities: List<*>) {
        logger.debug { "Find all entities of ${entityName(joinPoint)}. Total count of entities is ${entities.size}. User: ${user()}" }
    }

    /**
     * Validator/logger for find all methods
     *
     * @param joinPoint Join point object
     * @param page      page of entities
     */
    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.CrudService.page(..))", returning = "page")
    fun onAfterPage(joinPoint: JoinPoint, page: Page<*>) {
        logger.debug {
            "Page og entities of ${entityName(joinPoint)}. Page: ${page.number}. Total entities: ${page.totalElements}. User: ${user()}"
        }
    }

    /**
     * Validator/logger for getting one entity
     *
     * @param joinPoint Join point object
     * @param entity    Entity.
     */
    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.CrudService.findOne(..))", returning = "entity")
    fun onAfterFindOne(joinPoint: JoinPoint, entity: BaseEntity) {
        logger.debug {"Find one entity ${entityName(joinPoint)} with id ${entity.id}. User: ${user()}"}
    }

    /**
     * Validator/logger for save/update entity
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.CrudService.save(..))")
    fun onAfterSave(joinPoint: JoinPoint) {
        logger.debug {
            "Save/update entity/entities ${entityName(joinPoint)} with ID/IDs ${convertArguments(joinPoint)}. User: ${user()}"
        }
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
    fun onAfterSoftDelete(joinPoint: JoinPoint) {
        logger.debug {
            "Delete to archive entity/entities ${entityName(joinPoint)} with ID/IDs ${convertArguments(joinPoint)}. User: ${user()}"
        }
    }

    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.CrudService.softDeleteAll(..))")
    fun onAfterSoftDeleteAll(joinPoint: JoinPoint) {
        logger.debug {
            "Delete to archive entity/entities ${entityName(joinPoint)} with ID/IDs ${convertArguments(joinPoint)}. User: ${user()}"
        }
    }

    /**
     * Validator/logger for delete entity to archive
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.CrudService.delete(..))", returning = "entity")
    fun onAfterDelete(joinPoint: JoinPoint) {
        logger.debug {
            "Delete entity/entities ${entityName(joinPoint)} with ID/IDs ${convertArguments(joinPoint)}. User: ${user()}"
        }
    }

    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.CrudService.deleteAll(..))")
    fun onAfterDeleteAll(joinPoint: JoinPoint) {
        logger.debug {
            "Delete entity/entities ${entityName(joinPoint)} with ID/IDs ${convertArguments(joinPoint)}. User: ${user()}"
        }
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
    fun onAfterArchivedFindAll(joinPoint: JoinPoint, entities: List<*>) {
        logger.debug {
            "Find all archived entities of ${entityName(joinPoint)}. Total count of entities is ${entities.size}. User: ${user()}"
        }
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
    fun onAfterArchivedFindOne(joinPoint: JoinPoint, entity: BaseEntity) {
        logger.debug {
            "Find one archived entity ${entityName(joinPoint)} with id ${entity.id}. User: ${user()}"
        }
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
    fun onAfterRestore(joinPoint: JoinPoint) {
        logger.debug {
            "Restore from archive entity/entities ${entityName(joinPoint)} with ID/IDs ${convertArguments(joinPoint)}. User: ${user()}"
        }
    }

    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.ArchiveService.restoreAll(..))")
    fun onAfterRestoreAll(joinPoint: JoinPoint) {
        logger.debug {
            "Restore from archive entity/entities ${entityName(joinPoint)} with ID/IDs ${convertArguments(joinPoint)}. User: ${user()}"
        }
    }

    /**
     * Validator/logger for delete entity to archive
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.ArchiveService.delete(..))", returning = "entity")
    fun onAfterArchivedDelete(joinPoint: JoinPoint) {
        logger.debug {
            "Delete archived entity/entities ${entityName(joinPoint)} with ID/IDs ${convertArguments(joinPoint)}. User: ${user()}"
        }
    }

    @AfterReturning(pointcut = "execution(* io.alpere.crudfop.service.ArchiveService.deleteAll(..))")
    fun onAfterArchivedDeleteAll(joinPoint: JoinPoint) {
        logger.debug {
            "Delete archived entity/entities ${entityName(joinPoint)} with ID/IDs ${convertArguments(joinPoint)}. User: ${user()}"
        }
    }

    private fun entityName(joinPoint: JoinPoint): String {
        return joinPoint.let {
            it.target as AbstractService<*>
        }.entityName()
    }

    private fun convertArguments(joinPoint: JoinPoint): String {
        val arg = joinPoint.args[0]
        if (arg is BaseEntity) {
            return arg.id.toString()
        }
        if (arg is Iterable<*>) {
            return arg.joinToString {
                if (it is BaseEntity) {
                    it.id.toString()
                } else {
                    it.toString()
                }
            }
        }
        return arg.toString()
    }

    private fun user(): String {
        val user = auditorAware.user()
        return user.ifEmpty { "unknown user" }
    }
}