package de.thb.cloudsim.workload;

public class WorkloadFactory {

    public static WorkloadProfile createLightProfile() {
        WorkloadProfile profile = new WorkloadProfile("Light");

        profile.addRequestProfile(new RequestProfile(
                RequestType.STATIC_ASSET, 1, 3_000, 500, 1, 512, 0, 0
        ));
        profile.addRequestProfile(new RequestProfile(
                RequestType.GALLERY_VIEW, 2, 8_000, 1_000, 1, 1024, 2, 2_000
        ));
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_UPLOAD, 1, 10_000, 3_000, 1, 1024, 2, 4_000
        ));

        return profile;
    }

    public static WorkloadProfile createMediumProfile() {
        WorkloadProfile profile = new WorkloadProfile("Medium");

        profile.addRequestProfile(new RequestProfile(
                RequestType.STATIC_ASSET, 3, 3_000, 500, 1, 512, 0, 0
        ));
        profile.addRequestProfile(new RequestProfile(
                RequestType.GALLERY_VIEW, 5, 8_000, 1_000, 1, 1024, 2, 2_000
        ));
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_DETAIL, 2, 10_000, 1_500, 1, 1024, 1, 3_000
        ));
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_UPLOAD, 2, 12_000, 4_000, 1, 1024, 2, 5_000
        ));

        return profile;
    }

    public static WorkloadProfile createHeavyProfile() {
        WorkloadProfile profile = new WorkloadProfile("Heavy");

        profile.addRequestProfile(new RequestProfile(
                RequestType.STATIC_ASSET, 6, 3_000, 500, 1, 512, 0, 0
        ));
        profile.addRequestProfile(new RequestProfile(
                RequestType.GALLERY_VIEW, 8, 8_000, 1_000, 1, 1024, 2, 2_000
        ));
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_DETAIL, 4, 10_000, 1_500, 1, 1024, 1, 3_000
        ));
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_UPLOAD, 6, 12_000, 4_000, 1, 1024, 2, 5_000
        ));

        return profile;
    }
}