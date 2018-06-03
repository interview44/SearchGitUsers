package test44.searchgitusers.modals;

public class FollowersModal {
    String followerName;
    String followerAvatar;

    public FollowersModal(String followerName, String followerAvatar) {
        this.followerName = followerName;
        this.followerAvatar = followerAvatar;
    }


    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFollowerAvatar() {
        return followerAvatar;
    }

    public void setFollowerAvatar(String followerAvatar) {
        this.followerAvatar = followerAvatar;
    }
}
