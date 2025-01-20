package dev.tildejustin.infinity_lite;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.List;

public interface BookContent {
    BookContent EMPTY = new BookContent() {
        @Override
        public int getPageCount() {
            return 0;
        }

        @Override
        public Text getPageUnchecked(int number) {
            return new LiteralText("");
        }
    };

    static List<String> fromTag(CompoundTag tag) {
        ListTag tagList = tag.getList("pages", 8).copy();
        Builder<String> builder = ImmutableList.builder();

        for (int i = 0; i < tagList.size(); ++i) {
            builder.add(tagList.getString(i));
        }

        return builder.build();
    }

    static BookContent fromStack(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item == Items.WRITTEN_BOOK) {
            return new WrittenBookContents(itemStack);
        } else {
            return item == Items.WRITABLE_BOOK ? new WritableBookContents(itemStack) : EMPTY;
        }
    }

    int getPageCount();

    Text getPageUnchecked(int number);

    default Text getPage(int number) {
        return number >= 0 && number < this.getPageCount() ? this.getPageUnchecked(number) : new LiteralText("");
    }

    class WritableBookContents implements BookContent {
        private final List<String> pages;

        public WritableBookContents(ItemStack itemStack) {
            this.pages = getPages(itemStack);
        }

        private static List<String> getPages(ItemStack arg) {
            CompoundTag lv = arg.getTag();
            return lv != null ? BookContent.fromTag(lv) : ImmutableList.of();
        }

        @Override
        public int getPageCount() {
            return this.pages.size();
        }

        @Override
        public Text getPageUnchecked(int number) {
            return new LiteralText(this.pages.get(number));
        }
    }

    class WrittenBookContents implements BookContent {
        private final List<String> pages;

        public WrittenBookContents(ItemStack arg) {
            this.pages = getPages(arg);
        }

        private static List<String> getPages(ItemStack itemStack) {
            CompoundTag itemStackTag = itemStack.getTag();
            return WrittenBookItem.isValid(itemStackTag) ? BookContent.fromTag(itemStackTag) : ImmutableList.of(String.valueOf(new TranslatableText("book.invalid.tag").formatted(Formatting.DARK_RED)));
        }

        @Override
        public int getPageCount() {
            return this.pages.size();
        }

        @Override
        public Text getPageUnchecked(int number) {
            String string = this.pages.get(number);

            try {
                Text lv = Text.Serializer.fromJson(string);
                if (lv != null) {
                    return lv;
                }
            } catch (Exception ignored) {
            }

            return new LiteralText(string);
        }
    }
}
