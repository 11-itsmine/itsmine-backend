import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import axiosInstance from '../../api/axiosInstance';

const Category = ({ selectCategory, selectedCategory = {} }) => {
  const [categoryData, setCategoryData] = useState([]);
  const [isFilterTab, setIsFilterTab] = useState(true);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axiosInstance.get('/v1/categories');
        setCategoryData(response.data.data);
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };

    fetchCategories();
  }, []);

  const handleCategoryToggle = () => {
    setIsFilterTab(!isFilterTab);
  };

  const handleCategorySelect = (category) => {
    selectCategory(category);
  };

  return (
      <CategoryWrapper>
        <Title>
          <CategoryTag>카테고리</CategoryTag>
          <SeeMore onClick={handleCategoryToggle}>
            {isFilterTab ? '+' : '-'}
          </SeeMore>
        </Title>
        <List>
          {categoryData.map((category) => (
              <Hide key={category.id} isFilterTab={isFilterTab}>
                <SelectCategory
                    onClick={() => handleCategorySelect(category)}
                    isSelected={selectedCategory.categoryName === category.categoryName}
                >
                  <CheckBox
                      type="checkbox"
                      checked={selectedCategory.categoryName === category.categoryName}
                      readOnly
                  />
                  {category.categoryName}
                </SelectCategory>
              </Hide>
          ))}
        </List>
      </CategoryWrapper>
  );
};

export default Category;

const CategoryWrapper = styled.div`
  border-bottom: 1px solid lightgray;
  padding: 0.5rem 0.5rem;
`;

const Title = styled.div`
  display: flex;
  justify-content: space-between;
`;

const CategoryTag = styled.p`
  font-size: ${props => props.theme.fontSizes.small};
  margin: 0;
`;

const SeeMore = styled.button`
  background: none;
  border: none;
  font-size: ${props => props.theme.fontSizes.xxl};
  cursor: pointer;
`;

const List = styled.div`
  display: flex;
  flex-direction: column;
  color: ${props => props.theme.colors.black};
`;

const Hide = styled.div`
  display: ${props => (props.isFilterTab ? 'none' : 'block')};
`;

const SelectCategory = styled.div`
  padding: 0.5rem 0;
  background-color: ${({ isSelected }) => (isSelected ? '#d3d3d3' : 'transparent')}; /* 선택 시 배경색 변화 */
  cursor: pointer;

  &:hover {
    background-color: ${({ isSelected }) => (isSelected ? '#c0c0c0' : '#e0e0e0')};
  }
`;

const CheckBox = styled.input`
  transform: scale(1.5);
  margin-right: ${props => props.theme.margins.large};
`;
