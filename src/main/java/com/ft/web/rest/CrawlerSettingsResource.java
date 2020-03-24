package com.ft.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ft.domain.CrawlerSettings;
import com.ft.repository.CrawlerSettingsRepository;
import com.ft.service.mapper.CrawlConfigMapper;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.querydsl.core.types.Predicate;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ft.domain.CrawlerSettings}.
 */
@RestController
@RequestMapping("/api")
public class CrawlerSettingsResource {

    private final Logger log = LoggerFactory.getLogger(CrawlerSettingsResource.class);

    private static final String ENTITY_NAME = "a2p_rounting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrawlerSettingsRepository crawlerSettingsRepository;

    public CrawlerSettingsResource(CrawlerSettingsRepository crawlerSettingsRepository) {
        this.crawlerSettingsRepository = crawlerSettingsRepository;
    }

    /**
     * {@code POST  /crawler-settingss} : Create a new crawlerSettings.
     *
     * @param crawlerSettings the crawlerSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crawlerSettings, or with status {@code 400 (Bad Request)} if the crawlerSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crawler-settings")
    public ResponseEntity<CrawlerSettings> createCrawlerSettings(@Valid @RequestBody CrawlerSettings crawlerSettings, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save CrawlerSettings : {}", crawlerSettings);
        if (crawlerSettings.getId() != null) {
            throw new BadRequestAlertException("A new crawlerSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrawlerSettings result = crawlerSettingsRepository.save(crawlerSettings);
        return ResponseEntity.created(new URI("/api/crawler-settingss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crawler-settingss} : Updates an existing crawlerSettings.
     *
     * @param crawlerSettings the crawlerSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crawlerSettings,
     * or with status {@code 400 (Bad Request)} if the crawlerSettings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crawlerSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crawler-settings")
    public ResponseEntity<CrawlerSettings> updateCrawlerSettings(@Valid @RequestBody CrawlerSettings crawlerSettings, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update CrawlerSettings : {}", crawlerSettings);
        if (crawlerSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CrawlerSettings result = crawlerSettingsRepository.save(crawlerSettings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crawlerSettings.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /crawler-settingss} : get all the crawlerSettingss.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crawlerSettingss in body.
     */
    @GetMapping("/crawler-settings")
    public ResponseEntity<List<CrawlerSettings>> getAllCrawlerSettingss(Predicate predicate, Pageable pageable) {
        log.debug("REST request to get a page of CrawlerSettingss");
        Page<CrawlerSettings> page = predicate == null ? crawlerSettingsRepository.findAll(pageable) : crawlerSettingsRepository.findAll(predicate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crawler-settingss/:id} : get the "id" crawlerSettings.
     *
     * @param id the id of the crawlerSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crawlerSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crawler-settings/{id}")
    public ResponseEntity<CrawlerSettings> getCrawlerSettings(@PathVariable String id) {
        log.debug("REST request to get CrawlerSettings : {}", id);
        Optional<CrawlerSettings> crawlerSettings = crawlerSettingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(crawlerSettings);
    }

    /**
     * {@code DELETE  /crawler-settingss/:id} : delete the "id" crawlerSettings.
     *
     * @param id the id of the crawlerSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crawler-settings/{id}")
    public ResponseEntity<Void> deleteCrawlerSettings(@PathVariable String id, HttpServletRequest request) {
        log.debug("REST request to delete CrawlerSettings : {}", id);
        crawlerSettingsRepository.deleteById(id);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
    
    @Autowired
    CrawlConfigMapper mapper;
    /**
     * Return the default crawler settings
     * @param id
     * @return
     */
    @GetMapping("/public/crawler-settings")
    public ResponseEntity<CrawlerSettings> getPublicSettings() {
        log.debug("REST request to get default CrawlerSettings");
        return ResponseEntity.ok(mapper.toEntity(new CrawlConfig()));
    }
}
