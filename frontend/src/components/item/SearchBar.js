import React from 'react';
import styled from 'styled-components';

const SearchBar = ({handleInput}) => {
  return (
      <SearchWrapper onSubmit={handleInput}>
        <Input
            name="search"
            type="search"
            placeholder="Search and Enter"
            onFocus={e => {
              e.target.placeholder = '';
            }}
            onBlur={e => {
              e.target.placeholder = 'Search Anything';
            }}
            autoComplete="off"
        />
      </SearchWrapper>
  );
};

export default SearchBar;

const SearchWrapper = styled.form`
  width: 90vw;
  ${props => props.theme.flex.flexBox()}
  padding: 3rem 0 2rem;
  margin-top: 6rem;
`;

const Input = styled.input`
  width: 25rem;
  outline: 0;
  border-width: 0 0 5px;
  border-color: black;
  margin-top: ${props => props.theme.margins.xxl};
  text-align: center;
  font-size: ${props => props.theme.fontSizes.titleSize};

  :focus {
    border-color: navy;
  }
`;
