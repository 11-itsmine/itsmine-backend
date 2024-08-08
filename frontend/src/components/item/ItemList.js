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
import axiosInstance from '../../api/axiosInstance';

const ItemList = () => {
  const [productsList, setProductsList] = useState([]);
  const [selectCategory, setSelectCategory] = useState({});
  const [selectPrice, setSelectPrice] = useState({});
  const [userInput, setUserInput] = useState('');
  const [optionValue, setOptionValue] = useState('createdAt'); // Default sort by createdAt
  const [limit] = useState(8); // Number of items per page
  const [page, setPage] = useState(0); // Current page number
  const [isScrolled, setIsScrolled] = useState(false);

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

  const handleInput = (e) => {
    e.preventDefault();
    setUserInput(e.target.search.value);
    setPage(0); // Reset page on new search
  };

  const loadMore = () => {
    setPage((prevPage) => prevPage + 1);
  };

  useEffect(() => {
    const fetchProducts = async () => {
      const categoryString = selectCategory.query
          ? `&category=${selectCategory.query}`
          : '';
      const priceString = selectPrice.query ? `&price=${selectPrice.query}`
          : '';

      try {
        const response = await axiosInstance.get(
            `/v1/products/all?page=${page}&size=${limit}${categoryString}${priceString}&search=${userInput}&sort=${optionValue}`
        );

        if (!response.data.data.content) {
          throw new Error('Failed to fetch');
        }

        const data = response.data.data.content;
        console.log('Fetched data:', data);  // 여기서 데이터가 제대로 오는지 확인
        setProductsList((prevProducts) =>
            page === 0 ? data : [...prevProducts, ...data]
        );
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    fetchProducts();
  }, [selectCategory, selectPrice, userInput, optionValue, page, limit]);

  const handleCategory = (category) => {
    setSelectCategory((prevCategory) =>
        prevCategory.name === category.name ? {} : category
    );
    setPage(0); // Reset page on filter change
  };

  const handlePrice = (price) => {
    setSelectPrice((prevPrice) => (prevPrice.name === price.name ? {} : price));
    setPage(0); // Reset page on filter change
  };

  const deleteAllFilters = () => {
    setSelectCategory({});
    setSelectPrice({});
    setPage(0); // Reset page on filter reset
  };

  const deleteFilter = (selectedCategory) => {
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
            {BANNER_LIST.map((banner) => (
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
            <Category categorydata={CATEGORY_FILTER}
                      selectCategory={handleCategory}
                      filterSelect={selectCategory}/>
            <Price categorydata={PRICE_FILTER} selectPrice={handlePrice}
                   filterSelect={selectPrice}/>
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
                    onChange={(e) => {
                      setOptionValue(e.target.value);
                      setPage(0); // Reset page on sort change
                    }}
                >
                  {SORT_LIST.map((title) => (
                      <option key={title.id} value={title.value}>
                        {title.title}
                      </option>
                  ))}
                </Title>
              </SortingWrapper>
            </SearchOption>
            {Array.isArray(productsList) && productsList.length > 0 ? (
                <ItemWrapper>
                  <ItemsList>
                    {productsList.map((product) => {
                      const {
                        id,
                        productName,
                        description,
                        currentPrice,
                        auctionNowPrice,
                        dueDate,
                        imagesList,
                      } = product;
                      return (
                          <Item
                              key={id}
                              productName={productName}
                              description={description}
                              currentPrice={currentPrice}
                              auctionNowPrice={auctionNowPrice}
                              dueDate={dueDate}
                              imagesList={imagesList}
                              productId={id}
                          />
                      );
                    })}
                  </ItemsList>
                  <LoadMore onClick={loadMore}>더보기</LoadMore>
                </ItemWrapper>
            ) : (
                <ItemNotFound/>
            )}
          </ItemContainer>
        </Content>
        {isScrolled && <GoToTopBtn onClick={handleGoToTop}>&uArr;</GoToTopBtn>}
      </ContentWrapper>
  );
};

export default ItemList;

const ContentWrapper = styled.div`
  ${(props) => props.theme.flex.flexBox('column')}
`;

const BannerWrapper = styled.div`
  width: 72rem;
  margin-top: ${(props) => props.theme.margins.xxxl};
`;

const BannerList = styled.ul`
  ${(props) => props.theme.flex.flexBox('_', '_', 'space-between')}
`;

const Content = styled.div`
  ${(props) => props.theme.flex.flexBox('_', 'start')}
  box-sizing: border-box;
  padding: 3rem 2.5rem;
`;

const SearchFilter = styled.div`
  width: 15rem;
  margin-top: ${(props) => props.theme.margins.base};
`;

const Filter = styled.div`
  display: block;
  display: flex;
  width: 100%;
  margin-bottom: 1.5rem;
  font-size: ${({theme}) => theme.fontSizes.xs};
  font-weight: ${(props) => props.theme.fontWeights.semiBold};
  padding-left: ${(props) => props.theme.paddings.base};
`;

const FilterStatus = styled.div`
  width: 1.25rem;
  border: 1px solid black;
  border-radius: 0.625rem;
  margin-left: 0.625rem;
  text-align: center;
  background-color: ${(props) => props.theme.colors.black};
  color: white;
`;

const ItemContainer = styled.div`
  margin-left: 3rem;
  width: 60rem;
`;

const FilterDelete = styled.div`
  color: gray;
  border-bottom: 1px solid gray;
  padding-bottom: 0;
  margin-left: 4.375rem;
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
  background-color: #f4f4f4;
  border: 1px solid #f4f4f4;
  border-radius: 0.625rem;
  margin-left: ${(props) => props.theme.margins.large};
  padding: 0.313rem;
`;

const DeleteButton = styled.button`
  border-style: none;
  background-color: #f4f4f4;
`;

const SortingWrapper = styled.div`
  ${(props) => props.theme.flex.flexBox('_', '_', 'right')}
  padding: 1rem 0;
  padding-right: 1rem;
`;

const Title = styled.select`
  font-size: ${(props) => props.theme.fontSizes.small};
  text-align: center;
  padding: 0.5rem 1rem;
  margin-right: 0.1rem;
`;

const ItemWrapper = styled.div`
  ${(props) => props.theme.flex.flexBox('column')}
  margin-left: 1rem;
`;

const ItemsList = styled.div`
  display: grid;
  justify-items: center;
  grid-template-columns: repeat(4, 1fr);
  grid-column-gap: 1rem;
  padding: ${(props) => props.theme.paddings.base};
`;

const LoadMore = styled.button`
  width: 8rem;
  background: none;
  border: ${(props) => props.theme.borders.lightGray};
  border-radius: 1.25rem;
  font-weight: ${(props) => props.theme.fontWeights.thin};
  font-size: ${(props) => props.theme.fontSizes.small};
  padding: 0.9rem 1.5rem;
  cursor: pointer;

  :hover {
    background: ${(props) => props.theme.colors.lightGray};
  }
`;

const GoToTopBtn = styled.button`
  position: fixed;
  bottom: 2.5rem;
  right: 2.5rem;
  border: 1px solid lightgray;
  border-radius: 2.5rem;
  padding: 0.9rem 1rem;
  background: ${(props) => props.theme.colors.white};
  cursor: pointer;

  :hover {
    background: ${(props) => props.theme.colors.lightGray};
  }
`;
