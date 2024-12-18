package io.spring.application.article;

import io.spring.api.data.ArticleData;
import io.spring.application.ArticleQueryService;
import io.spring.application.CursorPageParameter;
import io.spring.application.CursorPager;
import io.spring.application.CursorPager.Direction;
import io.spring.application.DateTimeCursor;
import io.spring.application.Page;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.favorite.ArticleFavoriteRepository;
import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import io.spring.infrastructure.DbTestBase;
import io.spring.infrastructure.repository.MyBatisArticleFavoriteRepository;
import io.spring.infrastructure.repository.MyBatisArticleRepository;
import io.spring.infrastructure.repository.MyBatisUserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({
  ArticleQueryService.class,
  MyBatisUserRepository.class,
  MyBatisArticleRepository.class,
  MyBatisArticleFavoriteRepository.class
})
public class ArticleQueryServiceTest extends DbTestBase {
  @Autowired private ArticleQueryService queryService;

  @Autowired private ArticleRepository articleRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private ArticleFavoriteRepository articleFavoriteRepository;

  private User user;
  private Article article;

  @BeforeEach
  public void setUp() {
    user = User.of("aisensiy@gmail.com", "aisensiy", "123", "", "");
    userRepository.save(user);
    article =
        Article.of(
            "test", "desc", "body", Arrays.asList("java", "spring"), user.getId(), Instant.now());
    articleRepository.save(article);
  }

  @Test
  public void should_fetch_article_success() {
    Optional<ArticleData> optional = queryService.findById(article.getId(), user);
    Assertions.assertTrue(optional.isPresent());

    ArticleData fetched = optional.get();
    Assertions.assertEquals(fetched.getFavoritesCount(), 0);
    Assertions.assertFalse(fetched.isFavorited());
    Assertions.assertNotNull(fetched.getCreatedAt());
    Assertions.assertNotNull(fetched.getUpdatedAt());
    Assertions.assertTrue(fetched.getTagList().contains("java"));
  }

  @Test
  public void should_get_article_with_right_favorite_and_favorite_count() {
    User anotherUser = User.of("other@test.com", "other", "123", "", "");
    userRepository.save(anotherUser);
    articleFavoriteRepository.save(ArticleFavorite.of(article.getId(), anotherUser.getId()));

    Optional<ArticleData> optional = queryService.findById(article.getId(), anotherUser);
    Assertions.assertTrue(optional.isPresent());

    ArticleData articleData = optional.get();
    Assertions.assertEquals(articleData.getFavoritesCount(), 1);
    Assertions.assertTrue(articleData.isFavorited());
  }

  @Test
  public void should_get_default_article_list() {
    Article anotherArticle =
        Article.of(
            "Article.of",
            "desc",
            "body",
            Arrays.asList("test"),
            user.getId(),
            Instant.now().minus(1, ChronoUnit.HOURS));
    articleRepository.save(anotherArticle);

    ArticleDataList recentArticles =
        queryService.findRecentArticles(null, null, null, new Page(), user);
    Assertions.assertEquals(recentArticles.count(), 2);
    Assertions.assertEquals(recentArticles.articleDatas().size(), 2);
    Assertions.assertEquals(recentArticles.articleDatas().get(0).getId(), article.getId());

    ArticleDataList nodata =
        queryService.findRecentArticles(null, null, null, new Page(2, 10), user);
    Assertions.assertEquals(nodata.count(), 2);
    Assertions.assertEquals(nodata.articleDatas().size(), 0);
  }

  @Test
  public void should_get_default_article_list_by_cursor() {
    Article anotherArticle =
        Article.of(
            "Article.of",
            "desc",
            "body",
            Arrays.asList("test"),
            user.getId(),
            Instant.now().minus(1, ChronoUnit.HOURS));
    articleRepository.save(anotherArticle);

    CursorPager<ArticleData> recentArticles =
        queryService.findRecentArticlesWithCursor(
            null, null, null, new CursorPageParameter<>(null, 20, Direction.NEXT), user);
    Assertions.assertEquals(recentArticles.getData().size(), 2);
    Assertions.assertEquals(recentArticles.getData().get(0).getId(), article.getId());

    CursorPager<ArticleData> nodata =
        queryService.findRecentArticlesWithCursor(
            null,
            null,
            null,
            new CursorPageParameter<Instant>(
                DateTimeCursor.parse(recentArticles.getEndCursor().toString()), 20, Direction.NEXT),
            user);
    Assertions.assertEquals(nodata.getData().size(), 0);
    Assertions.assertEquals(nodata.getStartCursor(), null);

    CursorPager<ArticleData> prevArticles =
        queryService.findRecentArticlesWithCursor(
            null, null, null, new CursorPageParameter<>(null, 20, Direction.PREV), user);
    Assertions.assertEquals(prevArticles.getData().size(), 2);
  }

  @Test
  public void should_query_article_by_author() {
    User anotherUser = User.of("other@email.com", "other", "123", "", "");
    userRepository.save(anotherUser);

    Article anotherArticle =
        Article.of("Article.of", "desc", "body", Arrays.asList("test"), anotherUser.getId());
    articleRepository.save(anotherArticle);

    ArticleDataList recentArticles =
        queryService.findRecentArticles(null, user.getUsername(), null, new Page(), user);
    Assertions.assertEquals(recentArticles.articleDatas().size(), 1);
    Assertions.assertEquals(recentArticles.count(), 1);
  }

  @Test
  public void should_query_article_by_favorite() {
    User anotherUser = User.of("other@email.com", "other", "123", "", "");
    userRepository.save(anotherUser);

    Article anotherArticle =
        Article.of("Article.of", "desc", "body", Arrays.asList("test"), anotherUser.getId());
    articleRepository.save(anotherArticle);

    ArticleFavorite articleFavorite = ArticleFavorite.of(article.getId(), anotherUser.getId());
    articleFavoriteRepository.save(articleFavorite);

    ArticleDataList recentArticles =
        queryService.findRecentArticles(
            null, null, anotherUser.getUsername(), new Page(), anotherUser);
    Assertions.assertEquals(recentArticles.articleDatas().size(), 1);
    Assertions.assertEquals(recentArticles.count(), 1);
    ArticleData articleData = recentArticles.articleDatas().get(0);
    Assertions.assertEquals(articleData.getId(), article.getId());
    Assertions.assertEquals(articleData.getFavoritesCount(), 1);
    Assertions.assertTrue(articleData.isFavorited());
  }

  @Test
  public void should_query_article_by_tag() {
    Article anotherArticle =
        Article.of("Article.of", "desc", "body", Arrays.asList("test"), user.getId());
    articleRepository.save(anotherArticle);

    ArticleDataList recentArticles =
        queryService.findRecentArticles("spring", null, null, new Page(), user);
    Assertions.assertEquals(recentArticles.articleDatas().size(), 1);
    Assertions.assertEquals(recentArticles.count(), 1);
    Assertions.assertEquals(recentArticles.articleDatas().get(0).getId(), article.getId());

    ArticleDataList notag = queryService.findRecentArticles("notag", null, null, new Page(), user);
    Assertions.assertEquals(notag.count(), 0);
  }

  @Test
  public void should_show_following_if_user_followed_author() {
    User anotherUser = User.of("other@email.com", "other", "123", "", "");
    userRepository.save(anotherUser);

    FollowRelation followRelation = FollowRelation.of(anotherUser.getId(), user.getId());
    userRepository.saveRelation(followRelation);

    ArticleDataList recentArticles =
        queryService.findRecentArticles(null, null, null, new Page(), anotherUser);
    Assertions.assertEquals(recentArticles.count(), 1);
    ArticleData articleData = recentArticles.articleDatas().get(0);
    Assertions.assertTrue(articleData.getProfileData().isFollowing());
  }

  @Test
  public void should_get_user_feed() {
    User anotherUser = User.of("other@email.com", "other", "123", "", "");
    userRepository.save(anotherUser);

    FollowRelation followRelation = FollowRelation.of(anotherUser.getId(), user.getId());
    userRepository.saveRelation(followRelation);

    ArticleDataList userFeed = queryService.findUserFeed(user, new Page());
    Assertions.assertEquals(userFeed.count(), 0);

    ArticleDataList anotherUserFeed = queryService.findUserFeed(anotherUser, new Page());
    Assertions.assertEquals(anotherUserFeed.count(), 1);
    ArticleData articleData = anotherUserFeed.articleDatas().get(0);
    Assertions.assertTrue(articleData.getProfileData().isFollowing());
  }
}
