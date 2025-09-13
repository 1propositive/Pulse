package com.example.luto.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class EvidenceStorageService {

  private final S3Client s3;
  private final String bucket;
  private final boolean objectLockEnabled;

  public EvidenceStorageService(S3Client s3,
                                @Value("${S3_BUCKET:luto-evidencias}") String bucket,
                                @Value("${S3_OBJECT_LOCK_ENABLED:true}") boolean objectLockEnabled) {
    this.s3 = s3;
    this.bucket = bucket;
    this.objectLockEnabled = objectLockEnabled;
    ensureBucket();
  }

  private void ensureBucket() {
    try {
      s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
    } catch (S3Exception e) {
      CreateBucketRequest.Builder cb = CreateBucketRequest.builder().bucket(bucket);
      if (objectLockEnabled) {
        cb = cb.objectLockEnabledForBucket(true);
      }
      s3.createBucket(cb.build());
      s3.putBucketVersioning(PutBucketVersioningRequest.builder()
          .bucket(bucket)
          .versioningConfiguration(VersioningConfiguration.builder().status(BucketVersioningStatus.ENABLED).build())
          .build());
    }
  }

  public String putEvidence(String key, byte[] content, int retentionDays, Map<String,String> metadata) {
    PutObjectRequest.Builder po = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType("application/octet-stream");

    if (metadata != null && !metadata.isEmpty()) po = po.metadata(metadata);

    if (objectLockEnabled && retentionDays > 0) {
      po = po.objectLockMode(ObjectLockMode.GOVERNANCE)
            .objectLockRetainUntilDate(Instant.now().plus(retentionDays, ChronoUnit.DAYS));
    }

    PutObjectResponse resp = s3.putObject(po.build(), RequestBody.fromBytes(content));
    return resp.versionId();
  }

  public String putEvidenceJson(String key, String json, int retentionDays, Map<String,String> metadata) {
    return putEvidence(key, json.getBytes(StandardCharsets.UTF_8), retentionDays, metadata);
  }
}
