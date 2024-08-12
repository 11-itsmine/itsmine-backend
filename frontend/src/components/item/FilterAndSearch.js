import React, { useState } from 'react';
import Category from './Category';
import { CATEGORY_FILTER, PRICE_FILTER} from '../Data/categoryData';
import SORT_LIST from './SortListData';
import SearchBar from './SearchBar';

const FilterAndSearch = () => {
  const [selectedCategory, setSelectedCategory] = useState({});
  const [selectedSort, setSelectedSort] = useState(SORT_LIST[0]);

  const handleCategorySelect = category => {
    setSelectedCategory(category);
  };

  const handleSortSelect = sort => {
    setSelectedSort(sort);
  };

  const handleSearch = (e) => {
    e.preventDefault();
    const query = e.target.search.value;
    console.log('Searching for:', query, 'in category:', selectedCategory, 'sorted by:', selectedSort);
    // Add your search functionality here
  };

  return (
      <>
        <SearchBar handleInput={handleSearch} />
        <Category
            categorydata={CATEGORY_FILTER}
            selectCategory={handleCategorySelect}
            selectedCategory={selectedCategory}
        />
        <Category
            categorydata={PRICE_FILTER}
            selectCategory={handleCategorySelect}
            selectedCategory={selectedCategory}
        />
      </>
  );
};

export default FilterAndSearch;
