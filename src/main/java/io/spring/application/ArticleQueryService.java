package io.spring.application;

import static java.util.stream.Collectors.toList;

import io.spring.api.article.request.NewArticleParam;
import io.spring.api.article.request.UpdateArticleParam;
import io.spring.api.article.response.ArticleDataList;
import io.spring.api.article.response.ArticleFavoriteCount;
import io.spring.api.data.ArticleData;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.article.ArticleCommandService;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.favorite.ArticleFavoriteRepository;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import io.spring.infrastructure.mybatis.readservice.ArticleFavoritesReadService;
import io.spring.infrastructure.mybatis.readservice.ArticleReadService;
import io.spring.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleQueryService {
  private ArticleReadService articleReadService;
  private UserRelationshipQueryService userRelationshipQueryService;
  private ArticleFavoritesReadService articleFavoritesReadService;
  private ArticleRepository articleRepository;
  private ArticleFavoriteRepository articleFavoriteRepository;
  private ArticleCommandService articleCommandService;

  public ArticleData findById(String id, User user) {
    ArticleData articleData = articleReadService.findById(id);
    if (articleData == null || user == null) {
      throw new ResourceNotFoundException();
    }
    fillExtraInfo(id, user, articleData);
    return articleData;
  }

  public ArticleData findBySlug(String slug, User user) {
    ArticleData articleData = articleReadService.findBySlug(slug);
    if (articleData == null || user == null) {
      throw new ResourceNotFoundException();
    }
    fillExtraInfo(articleData.getId(), user, articleData);
    return articleData;
  }

  public CursorPager<ArticleData> findRecentArticlesWithCursor(
      String tag,
      String author,
      String favoritedBy,
      CursorPageParameter<Instant> page,
      User currentUser) {
    List<String> articleIds =
        articleReadService.findArticlesWithCursor(tag, author, favoritedBy, page);
    if (articleIds.size() == 0) {
      return new CursorPager<>(new ArrayList<>(), page.getDirection(), false);
    } else {
      boolean hasExtra = articleIds.size() > page.getLimit();
      if (hasExtra) {
        articleIds.remove(page.getLimit());
      }
      if (!page.isNext()) {
        Collections.reverse(articleIds);
      }

      List<ArticleData> articles = articleReadService.findArticles(articleIds);
      fillExtraInfo(articles, currentUser);

      return new CursorPager<>(articles, page.getDirection(), hasExtra);
    }
  }

  public CursorPager<ArticleData> findUserFeedWithCursor(
      User user, CursorPageParameter<Instant> page) {
    List<String> followdUsers = userRelationshipQueryService.followedUsers(user.getId());
    if (followdUsers.size() == 0) {
      return new CursorPager<>(new ArrayList<>(), page.getDirection(), false);
    } else {
      List<ArticleData> articles =
          articleReadService.findArticlesOfAuthorsWithCursor(followdUsers, page);
      boolean hasExtra = articles.size() > page.getLimit();
      if (hasExtra) {
        articles.remove(page.getLimit());
      }
      if (!page.isNext()) {
        Collections.reverse(articles);
      }
      fillExtraInfo(articles, user);
      return new CursorPager<>(articles, page.getDirection(), hasExtra);
    }
  }

  public ArticleDataList findRecentArticles(
      String tag, String author, String favoritedBy, Page page, User currentUser) {
    List<String> articleIds = articleReadService.queryArticles(tag, author, favoritedBy, page);
    int articleCount = articleReadService.countArticle(tag, author, favoritedBy);
    if (articleIds.size() == 0) {
      return new ArticleDataList(new ArrayList<>(), articleCount);
    } else {
      List<ArticleData> articles = articleReadService.findArticles(articleIds);
      fillExtraInfo(articles, currentUser);
      return new ArticleDataList(articles, articleCount);
    }
  }

  public ArticleDataList findUserFeed(User user, Page page) {
    List<String> followdUsers = userRelationshipQueryService.followedUsers(user.getId());
    if (followdUsers.size() == 0) {
      return new ArticleDataList(new ArrayList<>(), 0);
    } else {
      List<ArticleData> articles = articleReadService.findArticlesOfAuthors(followdUsers, page);
      fillExtraInfo(articles, user);
      int count = articleReadService.countFeedSize(followdUsers);
      return new ArticleDataList(articles, count);
    }
  }

  private void fillExtraInfo(List<ArticleData> articles, User currentUser) {
    setFavoriteCount(articles);
    if (currentUser != null) {
      setIsFavorite(articles, currentUser);
      setIsFollowingAuthor(articles, currentUser);
    }
  }

  private void setIsFollowingAuthor(List<ArticleData> articles, User currentUser) {
    Set<String> followingAuthors =
        userRelationshipQueryService.followingAuthors(
            currentUser.getId(),
            articles.stream()
                .map(articleData1 -> articleData1.getProfileData().getId())
                .collect(toList()));
    articles.forEach(
        articleData -> {
          if (followingAuthors.contains(articleData.getProfileData().getId())) {
            articleData.getProfileData().updateFollowing(true);
          }
        });
  }

  private void setFavoriteCount(List<ArticleData> articles) {
    List<ArticleFavoriteCount> favoritesCounts =
        articleFavoritesReadService.articlesFavoriteCount(
            articles.stream().map(ArticleData::getId).collect(toList()));
    Map<String, Integer> countMap = new HashMap<>();
    favoritesCounts.forEach(
        item -> {
          countMap.put(item.id(), item.count());
        });
    articles.forEach(
        articleData -> articleData.updateFavoritesCount(countMap.get(articleData.getId())));
  }

  private void setIsFavorite(List<ArticleData> articles, User currentUser) {
    Set<String> favoritedArticles =
        articleFavoritesReadService.userFavorites(
            articles.stream().map(articleData -> articleData.getId()).collect(toList()),
            currentUser);

    articles.forEach(
        articleData -> {
          if (favoritedArticles.contains(articleData.getId())) {
            articleData.updateFavorited(true);
          }
        });
  }

  private void fillExtraInfo(String id, User user, ArticleData articleData) {
    articleData.updateFavorited(articleFavoritesReadService.isUserFavorite(user.getId(), id));
    articleData.updateFavoritesCount(articleFavoritesReadService.articleFavoriteCount(id));
    articleData
        .getProfileData()
        .updateFollowing(
            userRelationshipQueryService.isUserFollowing(
                user.getId(), articleData.getProfileData().getId()));
  }

  // 추가
  public Article findBySlug(String slug) {
    return articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
  }

  public void removeArticle(Article article) {
    articleRepository.remove(article);
  }

  public void saveArticleFavorite(ArticleFavorite articleFavorite) {
    articleFavoriteRepository.save(articleFavorite);
  }

  public ArticleFavorite findArticleFavorite(String articleId, String userId) {
    return articleFavoriteRepository
        .find(articleId, userId)
        .orElseThrow(ResourceNotFoundException::new);
  }

  public void removeArticleFavorite(ArticleFavorite favorite) {
    articleFavoriteRepository.remove(favorite);
  }

  public ArticleData createArticle(NewArticleParam newArticleParam, User user) {
    Article article = articleCommandService.createArticle(newArticleParam, user);
    return findById(article.getId(), user);
  }

  public ArticleData updateArticle(String slug, User user, UpdateArticleParam updateArticleParam) {
    Article article = findBySlug(slug);
    if (!AuthorizationService.canWriteArticle(user, article)) {
      throw new NoAuthorizationException();
    }
    Article updatedArticle = articleCommandService.updateArticle(article, updateArticleParam);
    return findBySlug(updatedArticle.getSlug(), user);
  }

  public void deleteArticle(String slug, @AuthenticationPrincipal User user) {
    Article article = findBySlug(slug);
    if (!AuthorizationService.canWriteArticle(user, article)) {
      throw new NoAuthorizationException();
    }
    removeArticle(article);
  }

  public ArticleData saveFavoriteArticle(String slug, User user) {
    Article article = findBySlug(slug);
    ArticleFavorite articleFavorite = ArticleFavorite.of(article.getId(), user.getId());
    saveArticleFavorite(articleFavorite);
    return findBySlug(slug, user);
  }

  public ArticleData deleteFavoriteArticle(String slug, User user) {
    Article article = findBySlug(slug);
    ArticleFavorite favorite = findArticleFavorite(article.getId(), user.getId());
    removeArticleFavorite(favorite);
    return findBySlug(slug, user);
  }
}
