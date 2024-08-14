import React, { useState } from 'react';
import styled from 'styled-components';

const Category = ({ categorydata, selectCategory, filterSelect }) => {
  const [isFilterTab, setIsFilterTab] = useState(true);

  const { categoryList, categoryName } = categorydata;

  const handleCategoryClick = (category) => {
    selectCategory(category); // category의 id를 그대로 전달
  };

  return (
      <CategoryWrapper>
        <Title>
          <CategoryTag>{categoryName}</CategoryTag>
          <SeeMore onClick={() => setIsFilterTab(!isFilterTab)}>
            {isFilterTab ? '+' : '-'}
          </SeeMore>
        </Title>
        <List>
          {categoryList.map(category => (
              <Hide key={category.id} isFilterTab={isFilterTab}>
                <SelectCategory onClick={() => handleCategoryClick(category)}>
                  <CheckBox
                      type="checkBox"
                      checked={filterSelect.id === category.id}
                      readOnly
                  />
                  {category.name}
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
  padding: 1rem 0.5rem;
`;

const Title = styled.div`
  ${props => props.theme.flex.flexBox('_', '_', 'space-between')}
`;

const CategoryTag = styled.p`
  display: block;
  font-size: ${props => props.theme.fontSizes.small};
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

const SelectCategory = styled.div``;

const CheckBox = styled.input`
  border-color: ${props => props.theme.colors.gray};
  transform: scale(2);
  width: 0.625rem;
  height: 0.625rem;
  margin-right: ${props => props.theme.margins.large};
  margin-bottom: ${props => props.theme.margins.small};
`;
