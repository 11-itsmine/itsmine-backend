import React, {useEffect, useState} from 'react';
import styled from 'styled-components';
import Item from './Item';
import SearchBar from './SearchBar';
import Banner from './Banner';
import Category from './Category';
import Price from './Price';
import SORT_LIST from './SortListData';
import BANNER_LIST from '../Data/bannerListData';
import {CATEGORY_FILTER, PRICE_FILTER} from '../Data/categoryData';
import ItemNotFound from './ItemNotFound';

const ItemList = () => {
  const [productsList, setProductsList] = useState([]);
  const [selectCategory, setSelectCategory] = useState({});
  const [selectPrice, setSelectPrice] = useState({});
  const [userInput, setUserInput] = useState('');
  const [optionValue, setOptionValue] = useState('createdAt'); // Default sort by createdAt
  const [limit] = useState(8); // Number of items per page
  const [page, setPage] = useState(0); // Current page number
  const [isScrolled, setIsScrolled] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const handleScroll = () => {
      const isTop = window.scrollY < 500;
      setIsScrolled(!isTop);
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const handleGoToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  };

  const handleInput = e => {
    e.preventDefault();
    setUserInput(e.target.search.value);
    setPage(0); // Reset page on new search
  };

  const loadMore = () => {
    setPage(prevPage => prevPage + 1);
  };

  useEffect(() => {
    const fetchProducts = async () => {
      const categoryString = selectCategory.query
          ? `&category=${selectCategory.query}` : '';
      const priceString = selectPrice.query ? `&price=${selectPrice.query}`
          : '';

      try {
        setLoading(true);
        setError(null);

        const response = await fetch(
            `http://localhost:8080/products?page=${page}&size=${limit}${categoryString}${priceString}&search=${userInput}&sort=${optionValue}`,
            {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem(
                    'Authorization')}`,
              },
            }
        );

        if (!response.ok) {
          throw new Error('Failed to fetch');
        }

        const data = await response.json();
        setProductsList(
            prevProducts => (page === 0 ? data.content : [...prevProducts,
              ...data.content]));
      } catch (error) {
        setError('Failed to fetch products');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [selectCategory, selectPrice, userInput, optionValue, page, limit]);

  const handleCategory = category => {
    setSelectCategory(
        prevCategory => (prevCategory.name === category.name ? {} : category));
    setPage(0); // Reset page on filter change
  };

  const handlePrice = price => {
    setSelectPrice(prevPrice => (prevPrice.name === price.name ? {} : price));
    setPage(0); // Reset page on filter change
  };

  const deleteAllFilters = () => {
    setSelectCategory({});
    setSelectPrice({});
    setPage(0); // Reset page on filter reset
  };

  const deleteFilter = selectedCategory => {
    if (selectCategory === selectedCategory) {
      setSelectCategory({});
    }
    if (selectPrice === selectedCategory) {
      setSelectPrice({});
    }
    setPage(0); // Reset page on filter removal
  };

  const isCategorySelected = selectCategory.name || selectPrice.name;
  const totalFilter = !selectCategory.name && !selectPrice.name ? 0
      : selectCategory.name && selectPrice.name ? 2 : 1;

  return (
      <ContentWrapper>
        <SearchBar handleInput={handleInput} userInput={userInput}/>
        <BannerWrapper>
          <BannerList>
            {BANNER_LIST.map(banner => (
                <Banner key={banner.id} src={banner.src} text={banner.text}/>
            ))}
          </BannerList>
        </BannerWrapper>
        <Content>
          <SearchFilter>
            <Filter>
              필터
              {isCategorySelected && (
                  <>
                    <FilterStatus>{totalFilter}</FilterStatus>
                    <FilterDelete onClick={deleteAllFilters}>모두
                      삭제</FilterDelete>
                  </>
              )}
            </Filter>
            <Category
                categorydata={CATEGORY_FILTER}
                selectCategory={handleCategory}
                filterSelect={selectCategory}
            />
            <Price
                categorydata={PRICE_FILTER}
                selectPrice={handlePrice}
                filterSelect={selectPrice}
            />
          </SearchFilter>
          <ItemContainer>
            <SearchOption>
              <FilterCategorys>
                {selectCategory.name && (
                    <FilterCategory>
                      {selectCategory.name}
                      <DeleteButton onClick={() => deleteFilter(
                          selectCategory)}>X</DeleteButton>
                    </FilterCategory>
                )}
                {selectPrice.name && (
                    <FilterCategory>
                      {selectPrice.name}
                      <DeleteButton onClick={() => deleteFilter(
                          selectPrice)}>X</DeleteButton>
                    </FilterCategory>
                )}
              </FilterCategorys>
              <SortingWrapper>
                <Title
                    onChange={e => {
                      setOptionValue(e.target.value);
                      setPage(0); // Reset page on sort change
                    }}
                >
                  {SORT_LIST.map(title => (
                      <option key={title.id} value={title.value}>
                        {title.title}
                      </option>
                  ))}
                </Title>
              </SortingWrapper>
            </SearchOption>
            {loading && <Loading>Loading...</Loading>}
            {error && <Error>{error}</Error>}
            {productsList.length !== 0 ? (
                <ItemWrapper>
                  <ItemsList>
                    {productsList.map(product => {
                      const {
                        id,
                        productName,
                        description,
                        currentPrice,
                        getThumbnailUrl
                      } = product;
                      return (
                          <Item
                              key={id}
                              productName={productName}
                              description={description}
                              price={currentPrice}
                              thumbnail_url={getThumbnailUrl()}
                              productId={id}
                          />
                      );
                    })}
                  </ItemsList>
                  <LoadMore onClick={loadMore}>더보기</LoadMore>
                </ItemWrapper>
            ) : (
                !loading && <ItemNotFound/>
            )}
          </ItemContainer>
        </Content>
        {isScrolled && <GoToTopBtn onClick={handleGoToTop}>&uarr;</GoToTopBtn>}
      </ContentWrapper>
  );
};

export default ItemList;

const ContentWrapper = styled.div`
  display: flex;
  flex-direction: column;
`;

const BannerWrapper = styled.div`
  width: 72rem;
  margin-top: 2rem;
`;

const BannerList = styled.ul`
  display: flex;
  justify-content: space-between;
  list-style: none;
  padding: 0;
  margin: 0;
`;

const Content = styled.div`
  display: flex;
  align-items: flex-start;
  padding: 3rem 2.5rem;
  width: 100%;
  box-sizing: border-box;
`;

const SearchFilter = styled.div`
  width: 15rem;
  margin-top: 1rem;
`;

const Filter = styled.div`
  display: flex;
  align-items: center;
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
`;

const FilterStatus = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 1.25rem;
  height: 1.25rem;
  margin-left: 0.625rem;
  border: 1px solid black;
  border-radius: 50%;
  background-color: black;
  color: white;
`;

const ItemContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 3rem;
  width: 100%;
`;

const FilterDelete = styled.div`
  margin-left: auto;
  cursor: pointer;
  color: gray;
  border-bottom: 1px solid gray;
`;

const SearchOption = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
`;

const FilterCategorys = styled.div`
  display: flex;
  align-items: center;
`;

const FilterCategory = styled.div`
  display: flex;
  align-items: center;
  padding: 0.313rem;
  margin-left: 1rem;
  background-color: #f4f4f4;
  border: 1px solid #f4f4f4;
  border-radius: 0.625rem;
`;

const DeleteButton = styled.button`
  border: none;
  background-color: #f4f4f4;
  cursor: pointer;
`;

const SortingWrapper = styled.div`
  display: flex;
  align-items: center;
  padding: 1rem 0;
`;

const Title = styled.select`
  font-size: 1rem;
  padding: 0.5rem 1rem;
  margin-right: 0.5rem;
`;

const ItemWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 2rem;
`;

const ItemsList = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
  width: 100%;
`;

const LoadMore = styled.button`
  margin-top: 2rem;
  padding: 0.5rem 1rem;
  background-color: white;
  border: 1px solid gray;
  border-radius: 1rem;
  cursor: pointer;

  &:hover {
    background-color: lightgray;
  }
`;

const GoToTopBtn = styled.button`
  position: fixed;
  bottom: 2.5rem;
  right: 2.5rem;
  border: 1px solid lightgray;
  border-radius: 50%;
  padding: 0.5rem;
  background-color: white;
  cursor: pointer;

  &:hover {
    background-color: lightgray;
  }
`;

const Loading = styled.div`
  text-align: center;
  padding: 2rem;
`;

const Error = styled.div`
  color: red;
  text-align: center;
  padding: 2rem;
`;
